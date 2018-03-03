package com.toolslab.noisepercolator.view.messages

import android.view.View
import android.widget.TextView
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.model.Message
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify

class MessagesAdapterTest {

    companion object {
        private val MESSAGE1 = Message("address1", 1L, "body1", false, "")
        private val MESSAGE2 = Message("address2", 2L, "body2", true, "")
        private val MESSAGES = listOf(MESSAGE1, MESSAGE2)
    }

    private val mockViewHolder: MessagesAdapter.ViewHolder = mock()
    private val mockAddressTextView: TextView = mock()
    private val mockDateTextView: TextView = mock()
    private val mockBodyTextView: TextView = mock()
    private val mockSpamView: View = mock()

    private val underTest = MessagesAdapter(MESSAGES)

    @Before
    fun setUp() {
        whenever(mockViewHolder.address).thenReturn(mockAddressTextView)
        whenever(mockViewHolder.date).thenReturn(mockDateTextView)
        whenever(mockViewHolder.body).thenReturn(mockBodyTextView)
        whenever(mockViewHolder.spam).thenReturn(mockSpamView)
    }

    @Test
    fun onBindViewHolderFirst() {
        underTest.onBindViewHolder(mockViewHolder, 0)

        verify(mockAddressTextView).text = MESSAGE1.address
        verify(mockDateTextView).text = MESSAGE1.getFormattedDate()
        verify(mockBodyTextView).text = MESSAGE1.body + "\n" + "Debuginfo\n\t" + MESSAGE1.debugInfo.replace(";", "\n\t")
        verify(mockSpamView).visibility = View.INVISIBLE
    }

    @Test
    fun onBindViewHolderSecond() {
        underTest.onBindViewHolder(mockViewHolder, 1)

        verify(mockAddressTextView).text = MESSAGE2.address
        verify(mockDateTextView).text = MESSAGE2.getFormattedDate()
        verify(mockBodyTextView).text = MESSAGE2.body + "\n" + "Debuginfo\n\t" + MESSAGE2.debugInfo.replace(";", "\n\t")
        verify(mockSpamView).visibility = View.VISIBLE
    }

    @Test
    fun getItemCount() {
        val result = underTest.itemCount

        result shouldEqual MESSAGES.size
    }

}
