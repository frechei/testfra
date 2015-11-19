package org.haitao.common.logger;

public final class Settings {

  private int methodCount = 2;
  private int methodOffset = 0;
  private LogTool logTool;

  /**
   * Determines how logs will printed
   */
  private LogLevel logLevel = LogLevel.FULL;





  public Settings methodOffset(int offset) {
    this.methodOffset = offset;
    return this;
  }

  public Settings logTool(LogTool logTool) {
    this.logTool = logTool;
    return this;
  }

  public int getMethodCount() {
    return methodCount;
  }


  public LogLevel getLogLevel() {
    return logLevel;
  }

  public int getMethodOffset() {
    return methodOffset;
  }

  public LogTool getLogTool() {
    if (logTool == null) {
      logTool = new AndroidLogTool();
    }
    return logTool;
  }
}
