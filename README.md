### A simple annotation process that generated `String` key to `Enum Constant` and vise-versa mapping 

### Example Enum 1
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
class GenderTypeConverter {
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

### Example Enum 2
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
class UserRoleTypeConverter {
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