/*
 * Copyright (c) 2017. Manuel Rebollo Báez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mrebollob.m2p.domain.interactor

import com.mrebollob.m2p.domain.datasources.NetworkDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.domain.exceptions.InvalidCreditCardException
import com.mrebollob.m2p.domain.executor.PostExecutionThread
import com.mrebollob.m2p.domain.executor.ThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject

class GetCreditCardBalance @Inject constructor(val networkDataSource: NetworkDataSource,
                                               threadExecutor: ThreadExecutor,
                                               postExecutionThread: PostExecutionThread)
    : AbstractInteractor<CreditCardBalance, GetCreditCardBalance.Params>(threadExecutor, postExecutionThread) {

    override fun buildInteractorObservable(params: Params): Observable<CreditCardBalance> {

        if (params.creditCard.isNotValid()) {
            throw (InvalidCreditCardException())
        }

        return networkDataSource.getCreditCardBalance(params.creditCard)
    }

    class Params private constructor(val creditCard: CreditCard) {
        companion object {
            fun forCreditCard(creditCard: CreditCard): Params {
                return Params(creditCard)
            }
        }
    }
}