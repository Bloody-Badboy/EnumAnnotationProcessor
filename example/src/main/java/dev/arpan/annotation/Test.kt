package dev.arpan.annotation

import dev.arpan.annotation.processor.Key

enum class UserRole {
    @Key(value = "user")
    USER,

    @Key(value = "admin")
    ADMIN
}

enum class UserRoleIgnoreCase {
    @Key(value = "User", ignoreCase = true)
    USER,

    @Key(value = "Admin", ignoreCase = false)
    ADMIN
}

class Test {
    enum class Gender {
        @Key(value = "male", ignoreCase = true)
        MALE,

        @Key(value = "female")
        FEMALE,

        @Key(value = "other")
        OTHER
    }

    companion object {
        enum class UserRole {
            @Key(value = "User", ignoreCase = true)
            USER,

            @Key(value = "Admin", ignoreCase = false)
            ADMIN
        }
    }
}
