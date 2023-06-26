package dongwoongkim.springbootboard.helper;

import dongwoongkim.springbootboard.exception.category.CannotConvertNestedStructureException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


// K : 엔티티의 key 타입
// E : 엔티티의 타입
// D : 엔티티가 변환될 DTO의 타입
public class NestedConvertHelper<K,E,D> {
    private List<E> entities; // 플랫한 구조의 엔티티 목록
    private Function<E,D> toDto; // 하나의 엔티티로 하나의 Dto를 리턴해주는 함수
    private Function<E,E> getParent; // 하나의 엔티티의 부모 엔티티를 리턴해주는 함수
    private Function<E,K> getKey; // 하나의 엔티티의 Key(category_id)를 리턴해주는 함수
    private Function<D, List<D>> getChildren; // Dto의 children List를 리턴해주는 함수

    public static <K,E,D> NestedConvertHelper newInstance(List<E> entities, Function<E, D> toDto, Function<E, E> getParent, Function<E, K> getKey, Function<D, List<D>> getChildren) {
        return new NestedConvertHelper<K, E, D>(entities, toDto, getParent, getKey, getChildren);
    }
    public NestedConvertHelper(List<E> entities, Function<E, D> toDto, Function<E, E> getParent, Function<E, K> getKey, Function<D, List<D>> getChildren) {
        this.entities = entities;
        this.toDto = toDto;
        this.getParent = getParent;
        this.getKey = getKey;
        this.getChildren = getChildren;
    }

    public List<D> convert() {
        try {
            return convertInternal();
        } catch (NullPointerException e) {
            throw new CannotConvertNestedStructureException();
        }
    }

    private List<D> convertInternal() {
        Map<K, D> dtoMap = new HashMap<>();
        List<D> roots = new ArrayList<>();

        for (E e : entities) {
            D dto = toDto(e); // entity -> dto
            dtoMap.put(getKey(e), dto); // dto를 map에 put
            if (hasParent(e)) { // 부모 엔티티 있으면
                E parent = getParent(e);
                K parentKey = getKey(parent);
                D parentDto = dtoMap.get(parentKey); // 부모 엔티티를 dtoMap에서 꺼내 (findAllOrderByParentIdAscNullsFirstCategoryIdAsc() 할 예정이므로 반드시 있음)
                getChildren(parentDto).add(dto); // parentDto의 자식 리스트에 dto 추가
            } else {
                roots.add(dto); // 부모 엔티티 없으면 roots에 dto add
            }
        }
        return roots;
    }

    private boolean hasParent(E e) {
        return getParent(e) != null;
    }
    private D toDto(E e) {
        return toDto.apply(e);
    }

    private E getParent(E e) {
        return getParent.apply(e);
    }
    private K getKey(E e) {
        return getKey.apply(e);
    }

    private List<D> getChildren(D d) {
        return getChildren.apply(d);
    }

    // toDto -> getChildren


}
