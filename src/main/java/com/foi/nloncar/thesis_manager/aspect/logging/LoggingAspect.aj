package com.foi.nloncar.thesis_manager.aspect.logging;

import com.foi.nloncar.thesis_manager.exception.AuthenticationException;
import com.foi.nloncar.thesis_manager.exception.AuthorizationException;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.exception.ValidationException;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public aspect LoggingAspect {

	private final ThreadLocal<Deque<Long>> startTimes = ThreadLocal.withInitial(ArrayDeque::new);

	pointcut serviceMethods():
			execution(public * com.foi.nloncar.thesis_manager.rest..*Service.*(..));

	before(): serviceMethods() {
		Signature signature = thisJoinPoint.getSignature();
		Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());
		logger.info("[log] {} starting", signature.getName());

		startTimes.get().push(System.nanoTime());
	}

	after() returning(): serviceMethods() {
		Signature signature = thisJoinPoint.getSignature();
		Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());
		logger.info("[log] {} completed in {}ms", signature.getName(), elapsedMs());
	}

	after() throwing(Exception e): serviceMethods() {
		Signature signature = thisJoinPoint.getSignature();
		Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());
		long elapsed = elapsedMs();

		if (isDomainException(e)) {
			logger.warn("[log] {} failed after {}ms: {}", signature.getName(), elapsed, e.getMessage());
		} else {
			logger.error("[log] {} failed after {}ms", signature.getName(), elapsed, e);
		}
	}

	private long elapsedMs() {
		Long start = startTimes.get().poll();

		if (start == null) {
			return -1;
		}

		return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
	}

	private boolean isDomainException(Throwable e) {
		return e instanceof AuthenticationException
				|| e instanceof AuthorizationException
				|| e instanceof NotFoundException
				|| e instanceof ValidationException;
	}
}
