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

package com.mrebollob.m2p.presentation.view.main.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.mrebollob.m2p.R
import com.mrebollob.m2p.domain.entities.CreditCardMovement

class MovementViewHolder constructor(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    private val URL_PATTERN = "^(https?://)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([/\\w \\.-]*)*/?$".toRegex()
    private val nameTextView by lazy { itemView.findViewById(R.id.nameTv) as TextView }
    private val dateTextView by lazy { itemView.findViewById(R.id.dateTv) as TextView }
    private val amountTextView by lazy { itemView.findViewById(R.id.amountTv) as TextView }

    fun render(movement: CreditCardMovement) {
        nameTextView.text = formatString(movement.name)
        dateTextView.text = getContext().getString(R.string.date_format, movement.date)
        amountTextView.text = getContext().getString(R.string.money_format, movement.amount)

        if (movement.amount < 0) {
            amountTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.negative_value))
        } else {
            amountTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.positive_value))
        }
    }

    private fun getContext(): Context {
        return itemView.context
    }

    private fun formatString(name: String): String {
        if (URL_PATTERN.matches(name.toLowerCase())) {
            return name.toLowerCase()
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase()
        }
    }
}