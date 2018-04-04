package com.toolslab.noisepercolator.db

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.*
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.view.messages.MessageConverter
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Ignore
import org.junit.Test


class DataProviderTest {

    companion object {
        private const val MESSAGE_AS_STRING = "a message"
        private const val ANOTHER_MESSAGE_AS_STRING = "another message"
        private val MESSAGE = Message("an address", 0L, "a body")
        private val ANOTHER_MESSAGE = Message("another address", 1L, "another body")
        private val MESSAGES = mutableListOf(MESSAGE, ANOTHER_MESSAGE)
    }

    private val mockRealmWrapper: RealmWrapper = mock()
    private val mockRealm: Realm = mock()
    private val mockRealmQuery: RealmQuery<Message> = mock()
    private val mockRealmResults: RealmResults<Message> = mock()

    private val mockSharedPreferences: SharedPreferences = mock()
    private val mockEditor: SharedPreferences.Editor = mock()
    private val mockContext: Context = mock()
    private val mockMessageConverter: MessageConverter = mock()

    private val underTest = DataProvider(mockContext, mockMessageConverter, mockRealmWrapper)

    @Before
    fun setUp() {
        whenever(mockContext.getSharedPreferences(DataProvider.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)
        whenever(mockMessageConverter.convert(MESSAGE)).thenReturn(MESSAGE_AS_STRING)
        whenever(mockMessageConverter.convert(MESSAGE_AS_STRING)).thenReturn(MESSAGE)
        whenever(mockMessageConverter.convert(ANOTHER_MESSAGE)).thenReturn(ANOTHER_MESSAGE_AS_STRING)
        whenever(mockMessageConverter.convert(ANOTHER_MESSAGE_AS_STRING)).thenReturn(ANOTHER_MESSAGE)

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

//        testObserver.dispose()

//        verify(mockRealm).close() TODO Real method gets called
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
    fun getMessagesFromSharedPreferences() {
        val messages = setOf(MESSAGE_AS_STRING, ANOTHER_MESSAGE_AS_STRING)
        whenever(mockSharedPreferences.getStringSet(DataProvider.MESSAGES_KEY, emptySet())).thenReturn(messages)

        val result = underTest.getMessagesFromSharedPreferences()

        result.size shouldEqual messages.size
        result[0] shouldEqual MESSAGE
        result[1] shouldEqual ANOTHER_MESSAGE
    }

    @Test
    fun getMessagesDefault() {
        val result = underTest.getMessagesFromSharedPreferences()

        result shouldEqual emptyList()
    }

    @Test
    fun getMessagesStringSet() {
        val messages = setOf(MESSAGE_AS_STRING, ANOTHER_MESSAGE_AS_STRING)
        whenever(mockSharedPreferences.getStringSet(DataProvider.MESSAGES_KEY, emptySet())).thenReturn(messages)

        val result = underTest.getMessagesStringSet()

        result shouldEqual messages
    }

    @Test
    fun getMessagesStringSetDefault() {
        val result = underTest.getMessagesStringSet()

        result shouldEqual emptySet()
    }

    @Ignore("Complete implementation of saveMessage() test first")
    @Test
    fun saveMessages() {
        underTest.saveMessages(MESSAGES)
    }

    @Test
    fun saveMessage() {
        val captor = argumentCaptor<Realm.Transaction>()

        underTest.saveMessage(MESSAGE)

        inOrder(mockRealm).apply {
            verify(mockRealm).executeTransaction(captor.capture())

//            captor.firstValue.execute(mockRealm) TODO Real method gets called

//            verify(mockRealm).copyToRealm(MESSAGE)
//            verify(mockRealm).close()
        }
    }

    @Test
    fun saveMessageToSharedPreferences() {
        underTest.saveMessageToSharedPreferences(MESSAGE)

        inOrder(mockEditor).apply {
            verify(mockEditor).putStringSet(DataProvider.MESSAGES_KEY, setOf(MESSAGE_AS_STRING))
            verify(mockEditor).apply()
        }
    }

    @Test
    fun updateFromSharedPreferencesToRealm() {
        val originalMessages = setOf(MESSAGE_AS_STRING, ANOTHER_MESSAGE_AS_STRING)
        whenever(mockSharedPreferences.getStringSet(DataProvider.MESSAGES_KEY, emptySet())).thenReturn(originalMessages)
        whenever(mockSharedPreferences.contains(DataProvider.MESSAGES_KEY)).thenReturn(true)

        val result = underTest.getMessages()
        val messages = getResultFromObservable(result)

        // TODO Complete implementation of saveMessage() test first and then verify messages are saved

        inOrder(mockEditor).apply {
            verify(mockEditor).clear()
            verify(mockEditor).apply()
        }

        messages.size shouldEqual originalMessages.size
        messages[0] shouldEqual MESSAGE
        messages[1] shouldEqual ANOTHER_MESSAGE
    }

    @Test
    fun clearPreferences() {
        underTest.clearSharedPreferences()

        inOrder(mockEditor).apply {
            verify(mockEditor).clear()
            verify(mockEditor).apply()
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
