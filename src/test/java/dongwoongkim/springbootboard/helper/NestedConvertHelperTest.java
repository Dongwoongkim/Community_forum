package dongwoongkim.springbootboard.helper;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dongwoongkim.springbootboard.factory.category.CategoryFactory.createCategory;
import static org.assertj.core.api.Assertions.assertThat;

class NestedConvertHelperTest {

    private static class MyEntity {
        private Long id;
        private String name;
        private MyEntity parent;

        public MyEntity(Long id, String name, MyEntity parent) {
            this.id = id;
            this.name = name;
            this.parent = parent;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public MyEntity getParent() {
            return parent;
        }
    }

    private static class MyResponseDto { // 2
        private Long id;
        private String name;
        private List<MyResponseDto> children;

        public MyResponseDto(Long id, String name, List<MyResponseDto> children) {
            this.id = id;
            this.name = name;
            this.children = children;
        }

        public List<MyResponseDto> getChildren() {
            return children;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }


    @Test
    void convertTest() {
        // given
        // 1     NULL
        // 8     NULL
        // 2      1
        // 3      1
        // 4      2
        // 5      2
        // 7      3
        // 6      4
        MyEntity c1 = new MyEntity(1L, "category1", null);
        MyEntity c8 = new MyEntity(8L, "category8", null);
        MyEntity c2 = new MyEntity(2L, "category2", c1);
        MyEntity c3 = new MyEntity(3L, "category3", c1);
        MyEntity c4 = new MyEntity(4L, "category4", c2);
        MyEntity c5 = new MyEntity(5L, "category5", c2);
        MyEntity c7 = new MyEntity(7L, "category7", c3);
        MyEntity c6 = new MyEntity(6L, "category6", c4);

        List<MyEntity> myEntities = List.of(c1, c8, c2, c3, c4, c5, c7, c6);

        // then
        // 1
        //   2
        //     4
        //       6
        //     5
        //   3
        //     7
        // 8

        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                myEntities,
                c -> new MyResponseDto(c.getId(), c.getName(), new ArrayList<>()),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren()
        );

        List<MyResponseDto> result = helper.convert();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1L); // 1
        assertThat(result.get(0).getChildren().get(0).getId()).isEqualTo(2L); // 1-2
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getId()).isEqualTo(4L); // 1-2-4
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getId()).isEqualTo(6L); // 1-2-4-6
        assertThat(result.get(0).getChildren().get(0).getChildren().get(1).getId()).isEqualTo(5L); // 1-2-5
        assertThat(result.get(0).getChildren().get(1).getId()).isEqualTo(3L); // 1-3
        assertThat(result.get(0).getChildren().get(1).getChildren().get(0).getId()).isEqualTo(7L); // 1-3-7
        assertThat(result.get(1).getId()).isEqualTo(8L);
    }
}