### A simple annotation process that generated `String` key to `Enum Constant` and vise-versa mapping 

### Example 1
```kotlin
enum class Gender {
    @Key(value = "male", ignoreCase = true)
    MALE,

    @Key(value = "female")
    FEMALE,

    @Key(value = "other")
    OTHER
}
```
> Generated Class #1
```kotlin
@Generated("dev.arpan.annotation.processor.EnumAnnotationProcessor")
class GenderConverter {
  companion object {
    fun fromKey(value: String): Gender {
      if (value.equals("male", ignoreCase = true)) return Gender.MALE
      if (value.equals("female", ignoreCase = false)) return Gender.FEMALE
      if (value.equals("other", ignoreCase = false)) return Gender.OTHER
      throw IllegalArgumentException("Invalid value: $value")
    }

    fun toKey(value: Gender): String = when(value) {
      Gender.MALE -> "male"
      Gender.FEMALE -> "female"
      Gender.OTHER -> "other"
      else -> throw IllegalArgumentException("Invalid value: $value")
    }
  }
}

```

### Example 2
```kotlin
enum class UserRole {
    @Key(value = "user")
    USER,

    @Key(value = "admin")
    ADMIN
}
```
> Generated Class
```kotlin
@Generated("dev.arpan.annotation.processor.EnumAnnotationProcessor")
class UserRoleConverter {
  companion object {
    fun fromKey(value: String): UserRole = when(value) {
      "user" -> UserRole.USER
      "admin" -> UserRole.ADMIN
      else -> throw IllegalArgumentException("Invalid value: $value")
    }

    fun toKey(value: UserRole): String = when(value) {
      UserRole.USER -> "user"
      UserRole.ADMIN -> "admin"
      else -> throw IllegalArgumentException("Invalid value: $value")
    }
  }
}
```

### Example 3
```kotlin
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
```
> Generated Class
```kotlin
@Generated("dev.arpan.annotation.processor.EnumAnnotationProcessor")
class TestGenderConverter {
  companion object {
    fun fromKey(value: String): Test.Gender {
      if (value.equals("male", ignoreCase = true)) return Test.Gender.MALE
      if (value.equals("female", ignoreCase = false)) return Test.Gender.FEMALE
      if (value.equals("other", ignoreCase = false)) return Test.Gender.OTHER
      throw IllegalArgumentException("Invalid value: $value")
    }

    fun toKey(value: Test.Gender): String = when(value) {
      Test.Gender.MALE -> "male"
      Test.Gender.FEMALE -> "female"
      Test.Gender.OTHER -> "other"
      else -> throw IllegalArgumentException("Invalid value: $value")
    }
  }
}
```
```kotlin
@Generated("dev.arpan.annotation.processor.EnumAnnotationProcessor")
class TestCompanionUserRoleConverter {
  companion object {
    fun fromKey(value: String): Test.Companion.UserRole {
      if (value.equals("User", ignoreCase = true)) return Test.Companion.UserRole.USER
      if (value.equals("Admin", ignoreCase = false)) return Test.Companion.UserRole.ADMIN
      throw IllegalArgumentException("Invalid value: $value")
    }

    fun toKey(value: Test.Companion.UserRole): String = when(value) {
      Test.Companion.UserRole.USER -> "User"
      Test.Companion.UserRole.ADMIN -> "Admin"
      else -> throw IllegalArgumentException("Invalid value: $value")
    }
  }
}
```