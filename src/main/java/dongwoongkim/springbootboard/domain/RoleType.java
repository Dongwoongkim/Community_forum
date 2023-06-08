package dongwoongkim.springbootboard.domain;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleType {
    USER("ROLE_NORMAL"), MANAGER("ROLE_MANAGER"), ADMIN("ROLE_ADMIN");
    private final String value;
}
