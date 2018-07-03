package com.toolslab.noisepercolator.db

import com.nhaarman.mockito_kotlin.*
import com.toolslab.noisepercolator.model.Message
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test


class DataProviderTest {

    companion object {
        val MESSAGE = Message("an address", 0L, "a body")
        val ANOTHER_MESSAGE = Message("another address", 1L, "another body")
        val MESSAGES = mutableListOf(MESSAGE, ANOTHER_MESSAGE)
    }

    private val mockRealmWrapper: RealmWrapper = mock()
    private val mockRealm: Realm = mock()
    private val mockRealmQuery: RealmQuery<Message> = mock()
    private val mockRealmResults: RealmResults<Message> = mock()

    private val underTest = DataProvider(mockRealmWrapper)

    @Before
    fun setUp() {
        whenever(mockRealmWrapper.getDefaultInstance()).thenReturn(mockRealm)
        whenever(mockRealm.copyFromRealm(mockRealmResults)).thenReturn(MESSAGES)
        whenever(mockRealm.where(Message::class.java)).thenReturn(mockRealmQuery)
        whenever(mockRealmQuery.findAllAsync()).thenReturn(mockRealmResults)
        whenever(mockRealmResults.asFlowable()).thenReturn(Flowable.just(mockRealmResults))
    }

    @Test
    fun getMessages() {
        val result = underTest.getMessages()
        val messages = getResultFromObservable(result)

        messages.size shouldEqual MESSAGES.size
        messages[0] shouldEqual MESSAGE
        messages[1] shouldEqual ANOTHER_MESSAGE

        verify(mockRealm).where(Message::class.java)
        verify(mockRealm).copyFromRealm(mockRealmResults)
        verifyNoMoreInteractions(mockRealm)
    }

    @Test
    fun getMessagesOnDispose() {
        val observable = underTest.getMessages()
        val testObserver = TestObserver<List<Message>>()
        observable.subscribe(testObserver)

        verify(mockRealm).where(Message::class.java)
        verify(mockRealm).copyFromRealm(mockRealmResults)
        verifyNoMoreInteractions(mockRealm)

        testObserver.dispose()

        verify(mockRealm).close()
        verifyNoMoreInteractions(mockRealm)
    }

    @Test
    fun getMessagesOnError() {
        val throwableToReturn = Throwable()

        val result = underTest.getMessages()
        val error = getErrorFromObservable(result, throwableToReturn)

        verify(mockRealm).where(Message::class.java)
        verify(mockRealm).copyFromRealm(mockRealmResults)
        verifyNoMoreInteractions(mockRealm)
        error shouldEqual throwableToReturn
    }

    @Test
    fun saveMessage() {
        val captor = argumentCaptor<Realm.Transaction>()

        underTest.saveMessage(MESSAGE)

        inOrder(mockRealm).apply {
            verify(mockRealm).executeTransaction(captor.capture())
            verify(mockRealm).close()

            // Now make sure the lambda passed to executeTransaction() does the right thing
            captor.firstValue.execute(mockRealm)

            verify(mockRealm).copyToRealm(MESSAGE)
            verifyNoMoreInteractions(mockRealm)
        }
    }

    private fun <T> getResultFromObservable(observable: Observable<T>): T {
        val testObserver = TestObserver<T>()
        observable.subscribe(testObserver)
        testObserver.awaitTerminalEvent()

        testObserver.values().size shouldEqual 1
        testObserver.errors().size shouldEqual 0
        return testObserver.values()[0]
    }

    private fun <T> getErrorFromObservable(observable: Observable<T>, throwableToReturn: Throwable): Throwable {
        val testObserver = TestObserver<T>()
        observable.subscribe(testObserver)
        testObserver.awaitTerminalEvent()

        testObserver.onError(throwableToReturn)

        testObserver.values().size shouldEqual 1
        testObserver.errors().size shouldEqual 1
        return testObserver.errors()[0]
    }

}
