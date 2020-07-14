package com.make.equo.testing.common.statements;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.swt.widgets.Display;
import org.junit.runners.model.Statement;

public class RunInThreadStatement extends Statement {

	private final Statement baseStatement;
	private Future<?> future;
	private volatile Throwable throwable;
	private Display display;

	public RunInThreadStatement(Statement baseStatement, Display display) {
		this.baseStatement = baseStatement;
		this.display = display;
	}

	@Override
	public void evaluate() throws Throwable {
		ExecutorService executorService = runInThread();
		try {
			waitTillFinished();
		} finally {
			executorService.shutdown();
		}
		rethrowAssertionsAndErrors();
	}

	private ExecutorService runInThread() {
		ExecutorService result = Executors.newSingleThreadExecutor();
		future = result.submit(new Runnable() {
			@Override
			public void run() {
				try {
					baseStatement.evaluate();
				} catch (Throwable throwable) {
					RunInThreadStatement.this.throwable = throwable;
				}
			}
		});
		return result;
	}

	private void waitTillFinished() {
		while (!future.isDone()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void rethrowAssertionsAndErrors() throws Throwable {
		if (throwable != null) {
			throw throwable;
		}
	}
}
