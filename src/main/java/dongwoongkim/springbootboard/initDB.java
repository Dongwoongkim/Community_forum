package dongwoongkim.springbootboard;

import dongwoongkim.springbootboard.domain.category.Category;
import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.role.Role;
import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.RoleNotFoundException;
import dongwoongkim.springbootboard.repository.CategoryRepository;
import dongwoongkim.springbootboard.repository.MemberRepository;
import dongwoongkim.springbootboard.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static dongwoongkim.springbootboard.factory.CategoryFactory.createCategory;
import static dongwoongkim.springbootboard.factory.CategoryFactory.createCategoryWithName;

@Component
@RequiredArgsConstructor
@Slf4j
public class initDB {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        initRoles();
        initAdmin();
        initMember();
        initCategory();
    }

    @Transactional
    public void initRoles() {
        RoleType[] values = RoleType.values();
        List<Role> roles = Arrays.stream(values).map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
    }
    @Transactional
    public void initAdmin() {
        memberRepository.save(new Member(
                "admin", passwordEncoder.encode( "1q2w3e4r@@"), "IamAdmin", "admin@naver.com",
                List.of(roleRepository.findByRoleType(RoleType.ADMIN).orElseThrow(RoleNotFoundException::new),
                        roleRepository.findByRoleType(RoleType.MANAGER).orElseThrow(RoleNotFoundException::new),
                        roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)
                )));
    }
    @Transactional
    public void initMember() {
        memberRepository.save(new Member(
                "rlaehddnd", passwordEncoder.encode( "dhjrnfma1234!!"), "dongwoongkim", "rlaehddnd0422@naver.com",
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)
                )));
    }

    @Transactional
    public void initCategory() {
        // 1     NULL
        // 2      1
        List<Category> categories = new ArrayList<>();
        Category category1 = categoryRepository.save(createCategoryWithName("category1"));
        Category category2 = categoryRepository.save(createCategory("category2", category1));
    }


}
