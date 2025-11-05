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

## 주요 개발 내용

- `data`, `domain`, `ui` 계층으로 분리하여 유지보수성과 확장성을 고려한 구조 설계
- `Room`을 활용해 Todo 데이터의 CRUD 기능 및 데이터 영속성 구현
- `State`와 `ViewModel`을 활용해 UI 상태를 일관성 있게 관리하고, 데이터 변경에 따라 화면이 자동 갱신되는 반응형 구현
- `TodoItem`, `DrawerContent`, `HighlightedText` 등 재사용 가능한 컴포넌트 설계
- `Compose Navigation`을 적용해 화면 전환 흐름 구조화
- `SharedPreferences`를 활용해 다크 모드 설정 등 사용자 환경설정을 로컬에 저장하고, 앱 재실행 시에도 상태를 유지하도록 구현
- `MediaStore`, `PermissionHandler`를 활용한 파일 접근 및 저장 기능 구현
 
---

## 성과 및 기술 포인트
- 

---

## 배운 점
- 

---

## 앱 스크린샷
<!-- 홈 화면, 할 일 추가/삭제 화면, 테마 설정 화면 등 화면 추가 -->
-

---

## 개발자
**김주영(xxyoungkim)**  
- Email: [xxyoungkim@gmail.com](mailto:xxyoungkim@gmail.com)  
- GitHub: [https://github.com/xxyoungkim](https://github.com/xxyoungkim)  
