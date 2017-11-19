package com.masm.thread;

public interface Executor {

   void execute(Runnable runnable);

   void shutdown();
}
