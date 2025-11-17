package com.young.mytodo.di

import com.young.mytodo.data.repository.RoomTodoRepository
import com.young.mytodo.domain.repository.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository 의존성을 제공하는 Hilt Module
 * 인터페이스와 구현체를 바인딩
 * abstract class
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * TodoRepository 인터페이스와 RoomTodoRepository 구현체를 바인딩
     * @Binds - 인터페이스와 구현체를 연결할 때 사용
     * abstract 함수에 사용되며, 반환 타입이 인터페이스, 파라미터가 구현체
     */
    @Binds
    @Singleton
    abstract fun bindTodoRepository(
        roomTodoRepository: RoomTodoRepository
    ): TodoRepository
}