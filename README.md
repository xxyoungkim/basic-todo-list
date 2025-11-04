# ✨I CAN DO IT - 할 일 관리✨

> **Jetpack Compose**를 기반으로 구현한 일정 관리(To-Do) 앱입니다.

---

## 프로젝트 개요

일상 속 일정 관리를 돕는 To-Do 앱으로, Jetpack Compose 기반의 UI와 Room을 활용한 로컬 데이터 저장 구조를 구현했습니다.  
MVVM 패턴을 적용하여 UI와 데이터 계층의 의존성을 줄이고 유지보수성을 높였습니다.

---

## 기술 스택

| 구분 | 사용 기술 |
|------|------------|
| **언어** | Kotlin |
| **UI** | Jetpack Compose, Material3 |
| **데이터 관리** | Room |
| **비동기 처리** | Kotlin Coroutines, Flow |
| **도구** | Android Studio, Git, GitHub Actions |

---

## 프로젝트 구조

```plaintext
app
┣ data
┃ ┣ data_source
┃ ┃ ┣ TodoDao.kt
┃ ┃ ┗ TodoDatabase.kt
┃ ┗ repository
┃ ┃ ┗ RoomTodoRepository.kt
┣ domain
┃ ┣ model
┃ ┃ ┗ Todo.kt
┃ ┣ repository
┃ ┃ ┗ TodoRepository.kt
┃ ┗ util
┃ ┃ ┗ TodoAndroidViewModelFactory.kt
┣ ui
┃ ┣ main
┃ ┃ ┣ components
┃ ┃ ┃ ┣ DrawerContent.kt
┃ ┃ ┃ ┣ HighlightedText.kt
┃ ┃ ┃ ┗ TodoItem.kt
┃ ┃ ┣ HomeScreen.kt
┃ ┃ ┗ MainViewModel.kt
┃ ┣ settings
┃ ┃ ┣ util
┃ ┃ ┃ ┣ ThemeMode.kt
┃ ┃ ┃ ┗ ThemePreferences.kt
┃ ┃ ┣ SettingsScreen.kt
┃ ┃ ┗ SettingsThemeScreen.kt
┃ ┣ navigation
┃ ┃ ┗ TodoNavigation.kt
┃ ┗ theme
┃ ┃ ┣ Color.kt
┃ ┃ ┣ Theme.kt
┃ ┃ ┗ Type.kt
┣ util
┃ ┣ MediaStoreFileManager.kt
┃ ┗ PermissionHandler.kt
┗ MainActivity.kt
```

---

## 개발자
**김주영(xxyoungkim)**  
- Email: [xxyoungkim@gmail.com](mailto:xxyoungkim@gmail.com)  
- GitHub: [https://github.com/xxyoungkim](https://github.com/xxyoungkim)  
