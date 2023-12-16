package com.example.currencyconverter.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.R
import com.example.currencyconverter.api.NetworkResult
import com.example.currencyconverter.model.Eur
import com.example.currencyconverter.repository.BaseRepository
import com.example.currencyconverter.util.DispatchersProvider
import com.example.currencyconverter.util.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val baseRepository: BaseRepository,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    private val _currencyState = MutableStateFlow<CurrencyResultState>(CurrencyResultState.Nothing)
    val currencyState: StateFlow<CurrencyResultState> get() = _currencyState

    private fun setValueCurrencyState(
        currencyResultState: CurrencyResultState
    ) {
        _currencyState.value = currencyResultState
    }

    fun getCurrencies(
        currencyFrom: String,
        currencyTo: String,
        amountString: String
    ) = viewModelScope.launch(dispatchersProvider.io) {
        when (val amountDouble = amountString.toDoubleOrNull()) {
            null -> showError(R.string.amount_is_incorrect)
            else -> {
                setValueCurrencyState(CurrencyResultState.Loading)
                when (val networkResult = baseRepository.getCurrencies()) {
                    is NetworkResult.Failure -> showError(networkResult.message!!)
                    is NetworkResult.Success -> {
                        val getCurrenciesResponse = networkResult.data
                        val eur = getCurrenciesResponse?.eur
                        when {
                            eur != null -> {
                                val rateCurrencyBaseInEur = getRate(
                                    currencyTo = resourcesProvider.getString(R.string.eur),
                                    eur = eur
                                )
                                val rateCurrencyFromInEur = getRate(
                                    currencyTo = currencyFrom,
                                    eur = eur
                                )
                                val rateCurrencyToInEur = getRate(
                                    currencyTo = currencyTo,
                                    eur = eur
                                )
                                val rate = when {
                                    rateCurrencyBaseInEur != null &&
                                            rateCurrencyFromInEur != null &&
                                            rateCurrencyToInEur != null
                                    -> ((rateCurrencyBaseInEur / rateCurrencyFromInEur) * rateCurrencyToInEur) / rateCurrencyBaseInEur
                                    else -> null
                                }
                                when {
                                    rate != null -> {
                                        val result = BigDecimal((amountDouble * rate)).setScale(
                                            4,
                                            RoundingMode.HALF_UP
                                        ).toString()
                                        setValueCurrencyState(
                                            CurrencyResultState.Success(
                                                "$amountString ${currencyFrom.uppercase()} = $result ${currencyTo.uppercase()}"
                                            )
                                        )
                                    }
                                    else -> showError(R.string.error_occurred)
                                }
                            }
                            else -> showError(R.string.error_occurred)
                        }
                    }
                }
            }
        }
    }

    private fun showError(
        @StringRes messageInt: Int
    ) = setValueCurrencyState(
        CurrencyResultState.Error(resourcesProvider.getString(messageInt))
    )

    private fun showError(
        messageString: String
    ) = setValueCurrencyState(
        CurrencyResultState.Error(messageString)
    )

    private fun getRate(
        currencyTo: String,
        eur: Eur
    ): Double? = when (currencyTo) {
        resourcesProvider.getString(R.string._1inch) -> eur._1inch
        resourcesProvider.getString(R.string.ada) -> eur.ada
        resourcesProvider.getString(R.string.aed) -> eur.aed
        resourcesProvider.getString(R.string.afn) -> eur.afn
        resourcesProvider.getString(R.string.algo) -> eur.algo
        resourcesProvider.getString(R.string.all) -> eur.all
        resourcesProvider.getString(R.string.amd) -> eur.amd
        resourcesProvider.getString(R.string.ang) -> eur.ang
        resourcesProvider.getString(R.string.aoa) -> eur.aoa
        resourcesProvider.getString(R.string.ars) -> eur.ars
        resourcesProvider.getString(R.string.atom) -> eur.atom
        resourcesProvider.getString(R.string.aud) -> eur.aud
        resourcesProvider.getString(R.string.avax) -> eur.avax
        resourcesProvider.getString(R.string.awg) -> eur.awg
        resourcesProvider.getString(R.string.azn) -> eur.azn
        resourcesProvider.getString(R.string.bam) -> eur.bam
        resourcesProvider.getString(R.string.bbd) -> eur.bbd
        resourcesProvider.getString(R.string.bch) -> eur.bch
        resourcesProvider.getString(R.string.bdt) -> eur.bdt
        resourcesProvider.getString(R.string.bgn) -> eur.bgn
        resourcesProvider.getString(R.string.bhd) -> eur.bhd
        resourcesProvider.getString(R.string.bif) -> eur.bif
        resourcesProvider.getString(R.string.bmd) -> eur.bmd
        resourcesProvider.getString(R.string.bnb) -> eur.bnb
        resourcesProvider.getString(R.string.bnd) -> eur.bnd
        resourcesProvider.getString(R.string.bob) -> eur.bob
        resourcesProvider.getString(R.string.brl) -> eur.brl
        resourcesProvider.getString(R.string.bsd) -> eur.bsd
        resourcesProvider.getString(R.string.btc) -> eur.btc
        resourcesProvider.getString(R.string.btn) -> eur.btn
        resourcesProvider.getString(R.string.busd) -> eur.busd
        resourcesProvider.getString(R.string.bwp) -> eur.bwp
        resourcesProvider.getString(R.string.byn) -> eur.byn
        resourcesProvider.getString(R.string.byr) -> eur.byr
        resourcesProvider.getString(R.string.bzd) -> eur.bzd
        resourcesProvider.getString(R.string.cad) -> eur.cad
        resourcesProvider.getString(R.string.cdf) -> eur.cdf
        resourcesProvider.getString(R.string.chf) -> eur.chf
        resourcesProvider.getString(R.string.chz) -> eur.chz
        resourcesProvider.getString(R.string.clf) -> eur.clf
        resourcesProvider.getString(R.string.clp) -> eur.clp
        resourcesProvider.getString(R.string.cny) -> eur.cny
        resourcesProvider.getString(R.string.cop) -> eur.cop
        resourcesProvider.getString(R.string.crc) -> eur.crc
        resourcesProvider.getString(R.string.cro) -> eur.cro
        resourcesProvider.getString(R.string.cuc) -> eur.cuc
        resourcesProvider.getString(R.string.cup) -> eur.cup
        resourcesProvider.getString(R.string.cve) -> eur.cve
        resourcesProvider.getString(R.string.czk) -> eur.czk
        resourcesProvider.getString(R.string.dai) -> eur.dai
        resourcesProvider.getString(R.string.djf) -> eur.djf
        resourcesProvider.getString(R.string.dkk) -> eur.dkk
        resourcesProvider.getString(R.string.doge) -> eur.doge
        resourcesProvider.getString(R.string.dop) -> eur.dop
        resourcesProvider.getString(R.string.dot) -> eur.dot
        resourcesProvider.getString(R.string.dzd) -> eur.dzd
        resourcesProvider.getString(R.string.egld) -> eur.egld
        resourcesProvider.getString(R.string.egp) -> eur.egp
        resourcesProvider.getString(R.string.enj) -> eur.enj
        resourcesProvider.getString(R.string.ern) -> eur.ern
        resourcesProvider.getString(R.string.etb) -> eur.etb
        resourcesProvider.getString(R.string.etc) -> eur.etc
        resourcesProvider.getString(R.string.eth) -> eur.eth
        resourcesProvider.getString(R.string.eur) -> eur.eur
        resourcesProvider.getString(R.string.fil) -> eur.fil
        resourcesProvider.getString(R.string.fjd) -> eur.fjd
        resourcesProvider.getString(R.string.fkp) -> eur.fkp
        resourcesProvider.getString(R.string.ftt) -> eur.ftt
        resourcesProvider.getString(R.string.gbp) -> eur.gbp
        resourcesProvider.getString(R.string.gel) -> eur.gel
        resourcesProvider.getString(R.string.ggp) -> eur.ggp
        resourcesProvider.getString(R.string.ghs) -> eur.ghs
        resourcesProvider.getString(R.string.gip) -> eur.gip
        resourcesProvider.getString(R.string.gmd) -> eur.gmd
        resourcesProvider.getString(R.string.gnf) -> eur.gnf
        resourcesProvider.getString(R.string.grt) -> eur.grt
        resourcesProvider.getString(R.string.gtq) -> eur.gtq
        resourcesProvider.getString(R.string.gyd) -> eur.gyd
        resourcesProvider.getString(R.string.hkd) -> eur.hkd
        resourcesProvider.getString(R.string.hnl) -> eur.hnl
        resourcesProvider.getString(R.string.hrk) -> eur.hrk
        resourcesProvider.getString(R.string.htg) -> eur.htg
        resourcesProvider.getString(R.string.huf) -> eur.huf
        resourcesProvider.getString(R.string.icp) -> eur.icp
        resourcesProvider.getString(R.string.idr) -> eur.idr
        resourcesProvider.getString(R.string.ils) -> eur.ils
        resourcesProvider.getString(R.string.imp) -> eur.imp
        resourcesProvider.getString(R.string.inj) -> eur.inj
        resourcesProvider.getString(R.string.inr) -> eur.inr
        resourcesProvider.getString(R.string.iqd) -> eur.iqd
        resourcesProvider.getString(R.string.irr) -> eur.irr
        resourcesProvider.getString(R.string.isk) -> eur.isk
        resourcesProvider.getString(R.string.jep) -> eur.jep
        resourcesProvider.getString(R.string.jmd) -> eur.jmd
        resourcesProvider.getString(R.string.jod) -> eur.jod
        resourcesProvider.getString(R.string.jpy) -> eur.jpy
        resourcesProvider.getString(R.string.kes) -> eur.kes
        resourcesProvider.getString(R.string.kgs) -> eur.kgs
        resourcesProvider.getString(R.string.khr) -> eur.khr
        resourcesProvider.getString(R.string.kmf) -> eur.kmf
        resourcesProvider.getString(R.string.kpw) -> eur.kpw
        resourcesProvider.getString(R.string.krw) -> eur.krw
        resourcesProvider.getString(R.string.ksm) -> eur.ksm
        resourcesProvider.getString(R.string.kwd) -> eur.kwd
        resourcesProvider.getString(R.string.kyd) -> eur.kyd
        resourcesProvider.getString(R.string.kzt) -> eur.kzt
        resourcesProvider.getString(R.string.lak) -> eur.lak
        resourcesProvider.getString(R.string.lbp) -> eur.lbp
        resourcesProvider.getString(R.string.link) -> eur.link
        resourcesProvider.getString(R.string.lkr) -> eur.lkr
        resourcesProvider.getString(R.string.lrd) -> eur.lrd
        resourcesProvider.getString(R.string.lsl) -> eur.lsl
        resourcesProvider.getString(R.string.ltc) -> eur.ltc
        resourcesProvider.getString(R.string.ltl) -> eur.ltl
        resourcesProvider.getString(R.string.luna) -> eur.luna
        resourcesProvider.getString(R.string.lvl) -> eur.lvl
        resourcesProvider.getString(R.string.lyd) -> eur.lyd
        resourcesProvider.getString(R.string.mad) -> eur.mad
        resourcesProvider.getString(R.string.matic) -> eur.matic
        resourcesProvider.getString(R.string.mdl) -> eur.mdl
        resourcesProvider.getString(R.string.mga) -> eur.mga
        resourcesProvider.getString(R.string.mkd) -> eur.mkd
        resourcesProvider.getString(R.string.mmk) -> eur.mmk
        resourcesProvider.getString(R.string.mnt) -> eur.mnt
        resourcesProvider.getString(R.string.mop) -> eur.mop
        resourcesProvider.getString(R.string.mro) -> eur.mro
        resourcesProvider.getString(R.string.mur) -> eur.mur
        resourcesProvider.getString(R.string.mvr) -> eur.mvr
        resourcesProvider.getString(R.string.mwk) -> eur.mwk
        resourcesProvider.getString(R.string.mxn) -> eur.mxn
        resourcesProvider.getString(R.string.myr) -> eur.myr
        resourcesProvider.getString(R.string.mzn) -> eur.mzn
        resourcesProvider.getString(R.string.nad) -> eur.nad
        resourcesProvider.getString(R.string.ngn) -> eur.ngn
        resourcesProvider.getString(R.string.nio) -> eur.nio
        resourcesProvider.getString(R.string.nok) -> eur.nok
        resourcesProvider.getString(R.string.npr) -> eur.npr
        resourcesProvider.getString(R.string.nzd) -> eur.nzd
        resourcesProvider.getString(R.string.omr) -> eur.omr
        resourcesProvider.getString(R.string.one) -> eur.one
        resourcesProvider.getString(R.string.pab) -> eur.pab
        resourcesProvider.getString(R.string.pen) -> eur.pen
        resourcesProvider.getString(R.string.pgk) -> eur.pgk
        resourcesProvider.getString(R.string.php) -> eur.php
        resourcesProvider.getString(R.string.pkr) -> eur.pkr
        resourcesProvider.getString(R.string.pln) -> eur.pln
        resourcesProvider.getString(R.string.pyg) -> eur.pyg
        resourcesProvider.getString(R.string.qar) -> eur.qar
        resourcesProvider.getString(R.string.ron) -> eur.ron
        resourcesProvider.getString(R.string.rsd) -> eur.rsd
        resourcesProvider.getString(R.string.rub) -> eur.rub
        resourcesProvider.getString(R.string.rwf) -> eur.rwf
        resourcesProvider.getString(R.string.sar) -> eur.sar
        resourcesProvider.getString(R.string.sbd) -> eur.sbd
        resourcesProvider.getString(R.string.scr) -> eur.scr
        resourcesProvider.getString(R.string.sdg) -> eur.sdg
        resourcesProvider.getString(R.string.sek) -> eur.sek
        resourcesProvider.getString(R.string.sgd) -> eur.sgd
        resourcesProvider.getString(R.string.shib) -> eur.shib
        resourcesProvider.getString(R.string.shp) -> eur.shp
        resourcesProvider.getString(R.string.sll) -> eur.sll
        resourcesProvider.getString(R.string.sol) -> eur.sol
        resourcesProvider.getString(R.string.sos) -> eur.sos
        resourcesProvider.getString(R.string.srd) -> eur.srd
        resourcesProvider.getString(R.string.std) -> eur.std
        resourcesProvider.getString(R.string.svc) -> eur.svc
        resourcesProvider.getString(R.string.syp) -> eur.syp
        resourcesProvider.getString(R.string.szl) -> eur.szl
        resourcesProvider.getString(R.string.thb) -> eur.thb
        resourcesProvider.getString(R.string.theta) -> eur.theta
        resourcesProvider.getString(R.string.tjs) -> eur.tjs
        resourcesProvider.getString(R.string.tmt) -> eur.tmt
        resourcesProvider.getString(R.string.tnd) -> eur.tnd
        resourcesProvider.getString(R.string.top) -> eur.top
        resourcesProvider.getString(R.string.trx) -> eur.trx
        resourcesProvider.getString(R.string._try) -> eur._try
        resourcesProvider.getString(R.string.ttd) -> eur.ttd
        resourcesProvider.getString(R.string.twd) -> eur.twd
        resourcesProvider.getString(R.string.tzs) -> eur.tzs
        resourcesProvider.getString(R.string.uah) -> eur.uah
        resourcesProvider.getString(R.string.ugx) -> eur.ugx
        resourcesProvider.getString(R.string.uni) -> eur.uni
        resourcesProvider.getString(R.string.usd) -> eur.usd
        resourcesProvider.getString(R.string.usdc) -> eur.usdc
        resourcesProvider.getString(R.string.usdt) -> eur.usdt
        resourcesProvider.getString(R.string.uyu) -> eur.uyu
        resourcesProvider.getString(R.string.uzs) -> eur.uzs
        resourcesProvider.getString(R.string.vef) -> eur.vef
        resourcesProvider.getString(R.string.vet) -> eur.vet
        resourcesProvider.getString(R.string.vnd) -> eur.vnd
        resourcesProvider.getString(R.string.vuv) -> eur.vuv
        resourcesProvider.getString(R.string.wbtc) -> eur.wbtc
        resourcesProvider.getString(R.string.wst) -> eur.wst
        resourcesProvider.getString(R.string.xaf) -> eur.xaf
        resourcesProvider.getString(R.string.xag) -> eur.xag
        resourcesProvider.getString(R.string.xau) -> eur.xau
        resourcesProvider.getString(R.string.xcd) -> eur.xcd
        resourcesProvider.getString(R.string.xdr) -> eur.xdr
        resourcesProvider.getString(R.string.xlm) -> eur.xlm
        resourcesProvider.getString(R.string.xmr) -> eur.xmr
        resourcesProvider.getString(R.string.xof) -> eur.xof
        resourcesProvider.getString(R.string.xpf) -> eur.xpf
        resourcesProvider.getString(R.string.xrp) -> eur.xrp
        resourcesProvider.getString(R.string.yer) -> eur.yer
        resourcesProvider.getString(R.string.zar) -> eur.zar
        resourcesProvider.getString(R.string.zmk) -> eur.zmk
        resourcesProvider.getString(R.string.zmw) -> eur.zmw
        resourcesProvider.getString(R.string.zwl) -> eur.zwl
        else -> null
    }?.toDouble()

    val list = listOf(
        resourcesProvider.getString(R.string._1inch),
        resourcesProvider.getString(R.string.ada),
        resourcesProvider.getString(R.string.aed),
        resourcesProvider.getString(R.string.afn),
        resourcesProvider.getString(R.string.algo),
        resourcesProvider.getString(R.string.all),
        resourcesProvider.getString(R.string.amd),
        resourcesProvider.getString(R.string.ang),
        resourcesProvider.getString(R.string.aoa),
        resourcesProvider.getString(R.string.ars),
        resourcesProvider.getString(R.string.atom),
        resourcesProvider.getString(R.string.aud),
        resourcesProvider.getString(R.string.avax),
        resourcesProvider.getString(R.string.awg),
        resourcesProvider.getString(R.string.azn),
        resourcesProvider.getString(R.string.bam),
        resourcesProvider.getString(R.string.bbd),
        resourcesProvider.getString(R.string.bch),
        resourcesProvider.getString(R.string.bdt),
        resourcesProvider.getString(R.string.bgn),
        resourcesProvider.getString(R.string.bhd),
        resourcesProvider.getString(R.string.bif),
        resourcesProvider.getString(R.string.bmd),
        resourcesProvider.getString(R.string.bnb),
        resourcesProvider.getString(R.string.bnd),
        resourcesProvider.getString(R.string.bob),
        resourcesProvider.getString(R.string.brl),
        resourcesProvider.getString(R.string.bsd),
        resourcesProvider.getString(R.string.btc),
        resourcesProvider.getString(R.string.btn),
        resourcesProvider.getString(R.string.busd),
        resourcesProvider.getString(R.string.bwp),
        resourcesProvider.getString(R.string.byn),
        resourcesProvider.getString(R.string.byr),
        resourcesProvider.getString(R.string.bzd),
        resourcesProvider.getString(R.string.cad),
        resourcesProvider.getString(R.string.cdf),
        resourcesProvider.getString(R.string.chf),
        resourcesProvider.getString(R.string.chz),
        resourcesProvider.getString(R.string.clf),
        resourcesProvider.getString(R.string.clp),
        resourcesProvider.getString(R.string.cny),
        resourcesProvider.getString(R.string.cop),
        resourcesProvider.getString(R.string.crc),
        resourcesProvider.getString(R.string.cro),
        resourcesProvider.getString(R.string.cuc),
        resourcesProvider.getString(R.string.cup),
        resourcesProvider.getString(R.string.cve),
        resourcesProvider.getString(R.string.czk),
        resourcesProvider.getString(R.string.dai),
        resourcesProvider.getString(R.string.djf),
        resourcesProvider.getString(R.string.dkk),
        resourcesProvider.getString(R.string.doge),
        resourcesProvider.getString(R.string.dop),
        resourcesProvider.getString(R.string.dot),
        resourcesProvider.getString(R.string.dzd),
        resourcesProvider.getString(R.string.egld),
        resourcesProvider.getString(R.string.egp),
        resourcesProvider.getString(R.string.enj),
        resourcesProvider.getString(R.string.ern),
        resourcesProvider.getString(R.string.etb),
        resourcesProvider.getString(R.string.etc),
        resourcesProvider.getString(R.string.eth),
        resourcesProvider.getString(R.string.eur),
        resourcesProvider.getString(R.string.fil),
        resourcesProvider.getString(R.string.fjd),
        resourcesProvider.getString(R.string.fkp),
        resourcesProvider.getString(R.string.ftt),
        resourcesProvider.getString(R.string.gbp),
        resourcesProvider.getString(R.string.gel),
        resourcesProvider.getString(R.string.ggp),
        resourcesProvider.getString(R.string.ghs),
        resourcesProvider.getString(R.string.gip),
        resourcesProvider.getString(R.string.gmd),
        resourcesProvider.getString(R.string.gnf),
        resourcesProvider.getString(R.string.grt),
        resourcesProvider.getString(R.string.gtq),
        resourcesProvider.getString(R.string.gyd),
        resourcesProvider.getString(R.string.hkd),
        resourcesProvider.getString(R.string.hnl),
        resourcesProvider.getString(R.string.hrk),
        resourcesProvider.getString(R.string.htg),
        resourcesProvider.getString(R.string.huf),
        resourcesProvider.getString(R.string.icp),
        resourcesProvider.getString(R.string.idr),
        resourcesProvider.getString(R.string.ils),
        resourcesProvider.getString(R.string.imp),
        resourcesProvider.getString(R.string.inj),
        resourcesProvider.getString(R.string.inr),
        resourcesProvider.getString(R.string.iqd),
        resourcesProvider.getString(R.string.irr),
        resourcesProvider.getString(R.string.isk),
        resourcesProvider.getString(R.string.jep),
        resourcesProvider.getString(R.string.jmd),
        resourcesProvider.getString(R.string.jod),
        resourcesProvider.getString(R.string.jpy),
        resourcesProvider.getString(R.string.kes),
        resourcesProvider.getString(R.string.kgs),
        resourcesProvider.getString(R.string.khr),
        resourcesProvider.getString(R.string.kmf),
        resourcesProvider.getString(R.string.kpw),
        resourcesProvider.getString(R.string.krw),
        resourcesProvider.getString(R.string.ksm),
        resourcesProvider.getString(R.string.kwd),
        resourcesProvider.getString(R.string.kyd),
        resourcesProvider.getString(R.string.kzt),
        resourcesProvider.getString(R.string.lak),
        resourcesProvider.getString(R.string.lbp),
        resourcesProvider.getString(R.string.link),
        resourcesProvider.getString(R.string.lkr),
        resourcesProvider.getString(R.string.lrd),
        resourcesProvider.getString(R.string.lsl),
        resourcesProvider.getString(R.string.ltc),
        resourcesProvider.getString(R.string.ltl),
        resourcesProvider.getString(R.string.luna),
        resourcesProvider.getString(R.string.lvl),
        resourcesProvider.getString(R.string.lyd),
        resourcesProvider.getString(R.string.mad),
        resourcesProvider.getString(R.string.matic),
        resourcesProvider.getString(R.string.mdl),
        resourcesProvider.getString(R.string.mga),
        resourcesProvider.getString(R.string.mkd),
        resourcesProvider.getString(R.string.mmk),
        resourcesProvider.getString(R.string.mnt),
        resourcesProvider.getString(R.string.mop),
        resourcesProvider.getString(R.string.mro),
        resourcesProvider.getString(R.string.mur),
        resourcesProvider.getString(R.string.mvr),
        resourcesProvider.getString(R.string.mwk),
        resourcesProvider.getString(R.string.mxn),
        resourcesProvider.getString(R.string.myr),
        resourcesProvider.getString(R.string.mzn),
        resourcesProvider.getString(R.string.nad),
        resourcesProvider.getString(R.string.ngn),
        resourcesProvider.getString(R.string.nio),
        resourcesProvider.getString(R.string.nok),
        resourcesProvider.getString(R.string.npr),
        resourcesProvider.getString(R.string.nzd),
        resourcesProvider.getString(R.string.omr),
        resourcesProvider.getString(R.string.one),
        resourcesProvider.getString(R.string.pab),
        resourcesProvider.getString(R.string.pen),
        resourcesProvider.getString(R.string.pgk),
        resourcesProvider.getString(R.string.php),
        resourcesProvider.getString(R.string.pkr),
        resourcesProvider.getString(R.string.pln),
        resourcesProvider.getString(R.string.pyg),
        resourcesProvider.getString(R.string.qar),
        resourcesProvider.getString(R.string.ron),
        resourcesProvider.getString(R.string.rsd),
        resourcesProvider.getString(R.string.rub),
        resourcesProvider.getString(R.string.rwf),
        resourcesProvider.getString(R.string.sar),
        resourcesProvider.getString(R.string.sbd),
        resourcesProvider.getString(R.string.scr),
        resourcesProvider.getString(R.string.sdg),
        resourcesProvider.getString(R.string.sek),
        resourcesProvider.getString(R.string.sgd),
        resourcesProvider.getString(R.string.shib),
        resourcesProvider.getString(R.string.shp),
        resourcesProvider.getString(R.string.sll),
        resourcesProvider.getString(R.string.sol),
        resourcesProvider.getString(R.string.sos),
        resourcesProvider.getString(R.string.srd),
        resourcesProvider.getString(R.string.std),
        resourcesProvider.getString(R.string.svc),
        resourcesProvider.getString(R.string.syp),
        resourcesProvider.getString(R.string.szl),
        resourcesProvider.getString(R.string.thb),
        resourcesProvider.getString(R.string.theta),
        resourcesProvider.getString(R.string.tjs),
        resourcesProvider.getString(R.string.tmt),
        resourcesProvider.getString(R.string.tnd),
        resourcesProvider.getString(R.string.top),
        resourcesProvider.getString(R.string.trx),
        resourcesProvider.getString(R.string._try),
        resourcesProvider.getString(R.string.ttd),
        resourcesProvider.getString(R.string.twd),
        resourcesProvider.getString(R.string.tzs),
        resourcesProvider.getString(R.string.uah),
        resourcesProvider.getString(R.string.ugx),
        resourcesProvider.getString(R.string.uni),
        resourcesProvider.getString(R.string.usd),
        resourcesProvider.getString(R.string.usdc),
        resourcesProvider.getString(R.string.usdt),
        resourcesProvider.getString(R.string.uyu),
        resourcesProvider.getString(R.string.uzs),
        resourcesProvider.getString(R.string.vef),
        resourcesProvider.getString(R.string.vet),
        resourcesProvider.getString(R.string.vnd),
        resourcesProvider.getString(R.string.vuv),
        resourcesProvider.getString(R.string.wbtc),
        resourcesProvider.getString(R.string.wst),
        resourcesProvider.getString(R.string.xaf),
        resourcesProvider.getString(R.string.xag),
        resourcesProvider.getString(R.string.xau),
        resourcesProvider.getString(R.string.xcd),
        resourcesProvider.getString(R.string.xdr),
        resourcesProvider.getString(R.string.xlm),
        resourcesProvider.getString(R.string.xmr),
        resourcesProvider.getString(R.string.xof),
        resourcesProvider.getString(R.string.xpf),
        resourcesProvider.getString(R.string.xrp),
        resourcesProvider.getString(R.string.yer),
        resourcesProvider.getString(R.string.zar),
        resourcesProvider.getString(R.string.zmk),
        resourcesProvider.getString(R.string.zmw),
        resourcesProvider.getString(R.string.zwl)
    )
}