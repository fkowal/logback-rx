package com.malliina.logbackrx

import ch.qos.logback.classic.spi.ILoggingEvent
import com.malliina.logbackrx.RxLogback.{EventMapping, RxAppenderBase}
import com.malliina.rx.BoundedReplaySubject
import rx.lang.scala.Subject

/**
 * From http://logback.qos.ch/manual/appenders.html:
 *
 * All properties that follow the setter/getter JavaBeans conventions are handled transparently by logback configurators.
 *
 * Therefore, this appender uses Java-style setter/getters so that it can be configured thru logback.xml
 */
class BoundedReplayRxAppender[E] extends RxAppenderBase[E] {

  private var bufferSize = 1000
  private var innerSubject = BoundedReplaySubject[E](bufferSize)

  override protected def subject: Subject[E] = innerSubject

  def getBufferSize: Int = bufferSize

  def setBufferSize(size: Int): Unit = {
    bufferSize = size
    innerSubject = BoundedReplaySubject[E](bufferSize)
    // adds a log entry to logback's startup logging
    addInfo(s"Buffer size is set to $bufferSize")
  }
}

class BasicBoundedReplayRxAppender extends BoundedReplayRxAppender[ILoggingEvent] with EventMapping