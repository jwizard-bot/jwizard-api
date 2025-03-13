package pl.jwizard.jwa.core.util

import java.util.concurrent.Executors
import java.util.concurrent.Future

class BlockingThreadsExecutor<I : Any, O : Any>(
	poolSize: Int,
	private val futureCallback: (I) -> O,
) {
	private val executor = Executors.newFixedThreadPool(poolSize)
	private val futures = mutableListOf<Future<O>>()

	fun initThreads(inputData: List<I>) {
		for (inputElement in inputData) {
			futures += executor.submit<O> { futureCallback(inputElement) }
		}
	}

	fun waitAndGet(): List<O> {
		val calculatedData = futures.map { it.get() }
		executor.shutdown()
		return calculatedData
	}
}
