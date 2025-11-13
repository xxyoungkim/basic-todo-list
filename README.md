![header](https://capsule-render.vercel.app/api?type=waving&color=0:85b5fd,100:074CA1&height=200&section=header&text=I%20CAN%20DO%20IT&fontSize=55&fontColor=ffffff&fontAlignY=38)

# ✨I CAN DO IT - 할 일 관리✨
[![Google Play](https://img.shields.io/badge/Google%20Play-Download-brightgreen?logo=google-play&logoColor=white)](https://play.google.com/store/apps/details?id=com.young.mytodo)

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
<details>
<summary>📁 프로젝트 구조 보기 👀✨</summary>
<div markdown="1">
<br>
 
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
┃ ┃ ┣ SettingsExportScreen
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

</div>
</details>

---

## 주요 개발 내용

- `data`, `domain`, `ui` 계층으로 분리하여 **유지보수성과 확장성을 고려한 구조 설계**
- `Room`을 활용해 Todo 데이터의 CRUD 기능 및 데이터 영속성 구현
- `State`와 `ViewModel`을 활용해 **UI 상태를 일관성 있게 관리**하고, 데이터 변경에 따라 화면이 자동 갱신되는 **반응형** 구현
- `TodoItem`, `DrawerContent`, `HighlightedText` 등 **재사용 가능한 컴포넌트 설계**
- `Compose Navigation`을 적용해 화면 전환 흐름 구조화
- `SharedPreferences`를 활용해 다크 모드 설정 등 사용자 설정을 로컬에 저장하고, 앱 재실행 시에도 상태를 유지하도록 구현
- `MediaStore`, `PermissionHandler`를 활용한 파일 접근 및 저장 기능 구현
 
---

## 성과 및 기술 포인트
- `Jetpack Compose` 적용으로 기존 View 기반 대비 코드량 약 30% 감소 및 UI 변경 작업 시간 단축
- `Room + Flow`를 활용해 데이터 변경이 UI에 실시간 반영되는 **반응형 데이터 흐름 구축**, 앱 상태 관리 안정성 향상
- MVVM 패턴을 기반으로 **UI와 비즈니스 로직을 분리**하여 유지보수 시 코드 영향 범위를 최소화
- `MediaStore` 기반의 파일 저장 기능 구현으로 **Scoped Storage 정책에 대한 이해** 및 실제 환경 대응 능력 강화

---

## 앱 스크린샷
<!-- 홈 화면, 할 일 추가/삭제 화면, 테마 설정 화면 등 화면 추가 -->
-
