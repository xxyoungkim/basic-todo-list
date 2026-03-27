![header](https://capsule-render.vercel.app/api?type=waving&color=0:85b5fd,100:074CA1&height=200&section=header&text=I%20CAN%20DO%20IT&fontSize=55&fontColor=ffffff&fontAlignY=38)

# ✨I CAN DO IT - 투두리스트✨
[![Google Play](https://img.shields.io/badge/Google%20Play-Download-brightgreen?logo=google-play&logoColor=white)](https://play.google.com/store/apps/details?id=com.young.mytodo)

---

## 프로젝트 개요

Google Play에 출시한 Android 할 일 관리 앱입니다.  
Jetpack Compose 기반의 UI와 Room을 활용한 로컬 데이터 저장 구조를 구현했습니다.  

---

## 주요 화면
<!-- 홈 화면, 할 일 추가/삭제 화면, 테마 설정 화면 등 화면 추가 -->

| 홈 (달력형) | 홈 (목록형) | 할 일 등록 | 편집 및 메모 | 설정 |
|:---:|:---:|:---:|:---:|:---:|
| ![홈화면1](https://github.com/user-attachments/assets/7d9542bb-180a-444d-b785-116e97fca02c) | ![홈화면2](https://github.com/user-attachments/assets/a24231f3-7314-4055-8c1d-9a8acbfcec6b) | ![등록화면](https://github.com/user-attachments/assets/587cd064-e9a7-4c34-8b7c-3e4735844dd7) | ![수정화면](https://github.com/user-attachments/assets/b42b45b8-fff9-4cb3-8efa-96d8bcf86cf2) | ![설정화면](https://github.com/user-attachments/assets/8f6cafd0-3bcf-4aa7-959c-816dca643090) |

---

## 기술 스택

| 구분 | 사용 기술 |
|------|------------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose, Material3 |
| **DB** | Room |
| **DI** | Hilt |
| **Preference** | DataStore |
| **Async** | Kotlin Coroutines, StateFlow |
| **Analytics** | Firebase Analytics |
| **Tools** | Android Studio, Git, GitHub Actions |

---

## 프로젝트 구조

MVVM 구조를 적용하여 레이어 간 의존성을 줄이고 유지보수성을 높였습니다.

```plaintext
  app/
  ├── data/
  │   ├── data_source/     # Room DAO, Database
  │   └── repository/      # Repository 구현체 
  ├── domain/ 
  │   ├── model/           # Entity (Todo) 
  │   ├── repository/      # Repository 인터페이스 
  │   └── util/            # 도메인 유틸 
  ├── ui/  
  │   ├── main/            # HomeScreen, ViewModel, Components 
  │   ├── settings/        # 설정 화면들 
  │   └── navigation/      # NavHost, Routes  
  └── di/                  # Hilt 모듈
```

---

## 주요 기능

- **할일 CRUD** - 날짜 지정, 완료 토글
- **검색** - 실시간 키워드 검색
- **테마 설정** - 라이트/다크/시스템 연동, 색상 테마 선택
- **데이터 백업/복원** - UTF-8 텍스트 파일로 내보내기 및 가져오기
- **삭제 취소** - Snackbar Undo 패턴

---

## 주요 개발 내용

- `data`, `domain`, `ui` 계층으로 분리하여 **유지보수성과 확장성을 고려한 구조 설계**
- `Room`을 활용해 Todo 데이터의 CRUD 기능 및 데이터 영속성 구현
- `StateFlow`와 `ViewModel`을 활용해 **UI 상태를 일관성 있게 관리**하고, 데이터 변경에 따라 화면이 자동 갱신되는 **반응형** 구현
- `TodoItem`, `HighlightedText` 등 **재사용 가능한 컴포넌트 설계**
- `Compose Navigation`을 적용해 화면 전환 흐름 구조화
- `Hilt`를 활용한 의존성 주입으로 레이어 간 결합도를 낮추고 관심사 분리 구현
- `DataStore Preferences`를 활용해 사용자 설정을 로컬에 저장하고, 앱 재실행 시에도 상태를 유지하도록 구현
- `MediaStore API`를 활용한 데이터 백업/복원 기능 구현(Scoped Storage 정책 대응, UTF-8 BOM으로 한글 인코딩 처리, API 버전별 분기 처리)     

---

## 성과 및 기술 포인트
- `Jetpack Compose`의 선언형 UI 방식으로 상태 변화에 따른 UI 갱신을 자동화하여 UI 로직 복잡도 감소
- `Room + Flow`를 활용해 데이터 변경이 UI에 실시간 반영되는 **반응형 데이터 흐름 구축**, 앱 상태 관리 안정성 향상
- 추후 서버 연동 시 Data Layer의 코드 수정만으로 기능을 확장할 수 있도록 MVVM 패턴을 기반으로 **UI와 비즈니스 로직을 분리하여 설계**함으로써 코드 영향 범위를 최소화하고 유지보수성을 높임
- `MediaStore` 기반의 파일 저장 기능 구현으로 **Scoped Storage 정책에 대한 이해** 및 실제 환경 대응 능력 강화

---

## 기술적 의사결정

### 1. Sealed Class로 UI 상태 관리

Boolean 플래그 조합 대신 Sealed Class로 상태를 모델링하여 when 문에서 컴파일 타임에 모든 상태 처리를 강제합니다.

```kotlin       
sealed class ImportState {
    object Idle : ImportState()
    object Loading : ImportState()
    data class Confirm(val todos: List<Todo>) : ImportState()
    data class Success(val count: Int) : ImportState()  
    data class Error(val message: String) : ImportState()
}   
```

### 2. Room DB 마이그레이션 전략

이미 배포된 앱에서 스키마 변경 시 기존 사용자 데이터 손실을 방지하기 위해 증분 마이그레이션을 관리했습니다.

```kotlin
val migation3to4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {  
        database.execSQL("ALTER TABLE Todo ADD COLUMN memo TEXT")
    } 
}
```

### 3. 파싱 가능한 백업 파일 형식 설계

사람이 읽을 수 있으면서도 앱이 파싱 가능한 구조적 텍스트 형식을 직접 설계했습니다.  
ISO 8601 날짜 형식, Prefix 기반 파싱, UTF-8 BOM으로 한글 인코딩 이슈를 해결했습니다.

```plaintext  
[2026-01-01] 
 ✅ 완료된 할일  
 ⬜ 미완료 할일  
    메모: 메모 내용
```

### 4. Android API 버전 분기 처리 

minSdk 24 지원을 위해 구버전 API 분기를 처리했습니다.  
Locale.getDefault() 사용 시 일부 로케일에서 숫자 표기 차이로 날짜 파싱이 실패하는 문제를 Locale.ROOT로 해결했습니다.

```kotlin
// API 26+ : java.time (권장)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {               
    LocalDate.parse(dateStr).atStartOfDay(ZoneId.systemDefault())...
} else {
    // API 24~25 : SimpleDateFormat + Locale.ROOT  
    SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(dateStr)... 
}
```

### 5. MediaStore API로 파일 저장 (Android 10+)

Android 10의 Scoped Storage 정책에 따라 API 버전별 저장 방식을 분기하여 모든 버전에서 Downloads 폴더 저장을 지원합니다.    

```kotlin
// API 29+ : MediaStore (직접 파일 경로 접근 불가)
contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
// API 28 이하 : 직접 파일 시스템 접근
FileOutputStream(File(downloadsDir, fileName))
```

---
