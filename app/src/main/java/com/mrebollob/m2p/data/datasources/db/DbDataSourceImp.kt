/*
 * Copyright (c) 2016. Manuel Rebollo Báez
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

package com.mrebollob.m2p.data.datasources.db

import android.content.SharedPreferences
import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.exceptions.NoCreditCardException
import com.mrebollob.m2p.utils.encryption.Encryptor
import io.reactivex.Observable
import java.io.IOException
import javax.inject.Inject

class DbDataSourceImp @Inject constructor(val sharedPreferences: SharedPreferences,
                                          val encryptor: Encryptor) : DbDataSource {

    val CREDIT_CARD_NUMBER = "credit_card_number"
    val CREDIT_CARD_EXP_DATE = "credit_card_exp_date"

    override fun getCreditCard(): Observable<CreditCard> {
        return Observable.create {
            try {
                val number = encryptor
                        .getUnhashed(sharedPreferences.getString(CREDIT_CARD_NUMBER, ""))
                val expDate = encryptor
                        .getUnhashed(sharedPreferences.getString(CREDIT_CARD_EXP_DATE, ""))

                if (number.isNotBlank() && expDate.isNotBlank()) {
                    it.onNext(CreditCard(number, expDate, ""))
                    it.onComplete()
                } else {
                    it.onError(NoCreditCardException())
                }
            } catch (exception: IOException) {
                //TODO change exception
                it.onError(exception)
            }
        }
    }

    override fun removeCreditCard(): Observable<Unit> {
        return Observable.create {
            try {
                sharedPreferences.edit()
                        .clear()
                        .apply()
                it.onComplete()
            } catch (exception: IOException) {
                //TODO change exception
                it.onError(exception)
            }
        }
    }

    override fun createCreditCard(number: String, expDate: String): Observable<Unit> {
        return Observable.create {
            try {
                sharedPreferences.edit()
                        .putString(CREDIT_CARD_NUMBER, encryptor.getAsHash(number))
                        .putString(CREDIT_CARD_EXP_DATE, encryptor.getAsHash(expDate))
                        .apply()
                it.onComplete()
            } catch (exception: IOException) {
                //TODO change exception
                it.onError(exception)
            }
        }
    }
}