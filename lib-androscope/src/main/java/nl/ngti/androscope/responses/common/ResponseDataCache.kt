package nl.ngti.androscope.responses.common

import nl.ngti.androscope.server.SessionParams

internal class ResponseDataCache<Params, Data>(
        private val paramsSupplier: (SessionParams) -> Params,
        private val dataSupplier: (Params) -> Data,
        private val canUseData: (Data?) -> Boolean = { it != null },
        private val onAbandonData: ((Data?) -> Unit)? = null
) {

    private var lastParams: Params? = null
    private var lastCachedData: Data? = null
        set(value) {
            if (field !== value) {
                val oldData = field
                field = value
                onAbandonData?.run {
                    invoke(oldData)
                }
            }
        }

    operator fun get(session: SessionParams): Data {
        val params = paramsSupplier(session)
        return synchronized(this) {
            if (lastParams == params && canUseData(lastCachedData)) {
                lastCachedData!!
            } else {
                lastParams = params
                dataSupplier(params).apply {
                    lastCachedData = this
                }
            }
        }
    }
}
