<template>
  <q-page class="row justify-center q-py-lg">
    <div class="q-pa-md col-lg-8 col-md-8 col-sm-10 col-xs-12">

      <q-card class="my-card">
        <q-card-section class="bg-blue-grey-9 text-white">
          <div class="text-h6">{{ $t('signal.title') }}</div>
        </q-card-section>

        <q-card-section>
          <q-btn color="positive" icon-right="add" @click="newSignalDialog = true" :label="$t('signal.newSignal')"/>

          <q-dialog :dir="$i18n.locale === 'fa' ? 'rtl' : 'ltr'" v-model="newSignalDialog" persistent>
            <q-card style="width: 650px; max-width: 80vw;">
              <q-card-section>
                <div class="text-h6">{{ $t('signal.newSignal') }}</div>
              </q-card-section>

              <q-card-section class="q-pt-none">
                <q-select v-model="marketModel" :options="marketOptions" :label="$t('signal.market')"/>
                <q-select v-model="symbolModel" :options="symbolOptions" :label="$t('signal.symbol')"/>
                <q-select v-model="targetTypeModel" :options="targetTypeOptions" :label="$t('signal.targetType')"/>
                <q-input v-model="newTargetValueModel" type="number" :label="$t('signal.targetValue')"/>
                <q-input class="q-mb-sm" v-model="newMessageModel" :label="$t('signal.message')" :hint="$t('formHints.signal.message')" />
                <q-select class="q-mb-sm" v-model="newNotificationTypeModel" :options="notificationTypeOptions"
                          :label="$t('signal.notitficationType')" :hint="$t('formHints.signal.sendType')" />
                <q-select v-model="newTimeoutModel" :options="timeoutOptions" :label="$t('signal.timeout')"/>
              </q-card-section>
              <q-card-actions align="right" class="text-primary">
                <q-btn flat :label="$t('general.cancel')" v-close-popup/>
                <q-btn flat :label="$t('general.save')" @click="onCreateNewSignal" v-close-popup/>
              </q-card-actions>
            </q-card>
          </q-dialog>
        </q-card-section>

        <q-card-section v-show="!symbols || symbols.length === 0">
          <q-banner inline-actions rounded class="bg-blue-grey-7 text-white">
            {{ $t('signal.missingAnySignal') }}
          </q-banner>
        </q-card-section>

        <q-card-section>
          <q-list bordered class="rounded-borders">
            <template v-for="(symbol, index) in symbols" :key="`symbol-${symbols}-${index}`">
              <q-item>
                <q-item-section avatar top>
                  <q-avatar size="34px">
                    <img :src="$symbols[`${symbol.symbol}`]">
                  </q-avatar>
                </q-item-section>

                <q-item-section top class="col-2 gt-sm">
                  <q-item-label class="q-mt-sm">{{ symbol.symbol }}</q-item-label>
                </q-item-section>

                <q-item-section>
                  <q-item-label lines="1">
                    <span class="text-weight-medium">{{ $t('signal.numberOfSignals') }}</span>
                    <span class="text-weight-medium">: {{ symbol.signals }}</span>
                  </q-item-label>
                </q-item-section>

                <q-item-section top side>
                  <div class="q-gutter-xs">
                    <q-btn size="12px" flat dense round icon="edit"
                           @click="editSignalsDialog = true; editSignalSymbol = symbol.symbol">
                      <q-tooltip>
                        {{ $t('signal.editTooltip') }}
                      </q-tooltip>
                    </q-btn>
                  </div>
                </q-item-section>
              </q-item>
              <q-separator v-if="symbols.length - 1 > index" spaced/>
            </template>

            <q-dialog v-model="editSignalsDialog">
              <q-card style="width: 1400px; max-width: 80vw;">
                <q-card-section>
                </q-card-section>
                <q-card-section class="q-pt-none">
                  <div class="q-pa-md">
                    <q-markup-table>
                      <thead>
                      <tr>
                        <th class="text-left">{{ $t('signal.symbol') }}</th>
                        <th class="text-right">{{ $t('signal.targetType') }}</th>
                        <th class="text-right">{{ $t('signal.targetValue') }}</th>
                        <th class="text-right">{{ $t('signal.lastTriggered') }}</th>
                        <th class="text-right">{{ $t('signal.message') }}</th>
                        <th class="text-right">{{ $t('signal.notitficationType') }}</th>
                        <th class="text-right">{{ $t('signal.timeout') }}</th>
                        <th class="text-right">{{ $t('signal.enabled') }}</th>
                        <th class="text-right">{{ $t('general.operation') }}</th>
                      </tr>
                      </thead>
                      <tbody>
                      <tr v-for="signal in signals" :key="`signal-${signal.id}`">
                        <td class="text-left">{{ signal.symbol }}</td>
                        <td class="text-right">{{ signal.targetType }}</td>
                        <td class="text-right">{{ signal.targetValue }}</td>
                        <td class="text-right">{{ signal.lastTriggered }}</td>
                        <td class="text-right">{{ signal.message }}</td>
                        <td class="text-right">{{ signal.notificationType }}</td>
                        <td class="text-right">{{ timeoutOptions.find(option => option.value === signal.timeout).label }}</td>
                        <td class="text-right">
                          <q-toggle
                            v-model="signal.notificationEnabled"
                            checked-icon="check"
                            color="green"
                            unchecked-icon="clear"
                            disable
                          />
                        </td>
                        <td class="text-right">
                          <q-btn size="12px" flat dense round icon="edit" @click="onEditSignalBtn(signal.id)">
                            <q-tooltip>
                              {{ $t('signal.editTooltip') }}
                            </q-tooltip>
                          </q-btn>
                          <q-btn size="12px" flat dense round icon="delete"
                                 @click="deleteSignalId = signal.id; deleteSignalDialog = true">
                            <q-tooltip>
                              {{ $t('general.delete') }}
                            </q-tooltip>
                          </q-btn>
                        </td>
                      </tr>
                      </tbody>
                    </q-markup-table>

                    <div class="q-pt-lg flex flex-center">
                      <q-pagination
                        v-model="signalCurrentPage"
                        :max="signalTotalPage"
                        :max-pages="6"
                        boundary-numbers
                      />
                    </div>

                    <q-dialog :dir="$i18n.locale === 'fa' ? 'rtl' : 'ltr'" v-model="deleteSignalDialog" persistent>
                      <q-card>
                        <q-card-section class="row items-center">
                          <q-avatar icon="delete" color="negative" text-color="white"/>
                          <span class="q-ml-sm">{{ $t('general.deleteDialog') }}</span>
                        </q-card-section>

                        <q-card-actions align="right">
                          <q-btn flat :label="$t('general.cancel')" color="primary" v-close-popup/>
                          <q-btn flat :label="$t('general.delete')" color="primary" @click="onDeleteSignal" v-close-popup/>
                        </q-card-actions>
                      </q-card>
                    </q-dialog>

                    <q-dialog :dir="$i18n.locale === 'fa' ? 'rtl' : 'ltr'" v-model="updateSignalDialog" persistent>
                      <q-card style="width: 650px; max-width: 80vw;">
                        <q-card-section>
                          <div class="text-h6">{{ $t('signal.updateSignal') }}</div>
                        </q-card-section>

                        <q-card-section class="q-pt-none">
                          <q-toggle v-model="updateNotificationEnableModel" :left-label="true"
                                    :label="$t('signal.enabled')" unchecked-icon="clear" checked-icon="check"
                                    color="green"/>
                          <q-input v-model="marketName" :label="$t('signal.market')" disable/>
                          <q-input v-model="editSignal.symbol" :label="$t('signal.symbol')" disable/>
                          <q-input v-model="editSignal.targetType" :label="$t('signal.targetType')" disable/>
                          <q-input v-model="updateTargetValueModel" type="number" :label="$t('signal.targetValue')"/>
                          <q-input v-model="updateMessageModel" :label="$t('signal.message')"/>
                          <q-select v-model="updateNotificationTypeModel" :options="notificationTypeOptions"
                                    :label="$t('signal.notitficationType')"/>
                          <q-select v-model="updateTimeoutModel" :options="timeoutOptions" :label="$t('signal.timeout')"/>
                        </q-card-section>
                        <q-card-actions align="right" class="text-primary">
                          <q-btn flat :label="$t('general.cancel')" v-close-popup/>
                          <q-btn flat :label="$t('general.save')" @click="onUpdateSignal" v-close-popup/>
                        </q-card-actions>
                      </q-card>
                    </q-dialog>
                  </div>
                </q-card-section>

                <q-card-actions align="right" class="text-primary">
                  <q-btn flat :label="$t('general.close')" v-close-popup/>
                </q-card-actions>
              </q-card>
            </q-dialog>

          </q-list>
        </q-card-section>

        <q-card-section>
          <div class="q-py-sm flex flex-center">
            <q-pagination
              v-model="symbolCurrentPage"
              :max="symbolTotalPage"
              :max-pages="6"
              boundary-numbers
            />
          </div>
        </q-card-section>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import moment from "moment/moment";

export default {
  name: "SignalPage",

  data() {
    return {
      editSignalsDialog: false,
      newSignalDialog: false,
      marketOptions: [],
      marketModel: undefined,
      symbolModel: undefined,
      symbolOptions: [],
      targetTypeOptions: [
        {label: `${this.$t('formHints.signal.maxPrice')}`, value: 'MaxPrice'},
        {label: `${this.$t('formHints.signal.minPrice')}`, value: 'MinPrice'},
        {label: `${this.$t('formHints.signal.price')}`, value: 'Price'},
        {label: `${this.$t('formHints.signal.maxChange')}`, value: 'MaxChange'},
        {label: `${this.$t('formHints.signal.minChange')}`, value: 'MinChange'},
        {label: `${this.$t('formHints.signal.change')}`, value: 'Change'},
        {label: `${this.$t('formHints.signal.volume')}`, value: 'Volume'}],
      targetTypeModel: undefined,
      newTargetValueModel: undefined,
      newMessageModel: undefined,
      newNotificationTypeModel: undefined,
      notificationTypeOptions: ['EMAIL', 'TELEGRAM'],
      newTimeoutModel: undefined,
      updateSignalDialog: false,
      updateTargetValueModel: undefined,
      updateMessageModel: undefined,
      updateNotificationTypeModel: undefined,
      updateNotificationEnableModel: undefined,
      updateTimeoutModel: undefined,
      symbols: [],
      signals: [],
      editSignalSymbol: undefined,
      editSignalId: undefined,
      editSignal: undefined,
      marketName: undefined,
      symbolCurrentPage: 1,
      symbolTotalPage: 1,
      signalCurrentPage: 1,
      signalTotalPage: 1,
      markets: [],
      assets: [],
      deleteSignalDialog: false,
      deleteSignalId: undefined,
      timeoutOptions: [
        {label: `${this.$t('formHints.signal.timeout.zero')}`, value: 1},
        {label: `${this.$t('formHints.signal.timeout.oneMin')}`, value: 60},
        {label: `${this.$t('formHints.signal.timeout.fiveMin')}`, value: 300},
        {label: `${this.$t('formHints.signal.timeout.tenMin')}`, value: 600},
        {label: `${this.$t('formHints.signal.timeout.fifteenMin')}`, value: 900},
        {label: `${this.$t('formHints.signal.timeout.thirtyMin')}`, value: 1800},
        {label: `${this.$t('formHints.signal.timeout.oneHour')}`, value: 3600},
        {label: `${this.$t('formHints.signal.timeout.fourHour')}`, value: 14400},
        {label: `${this.$t('formHints.signal.timeout.eightHour')}`, value: 28800},
        {label: `${this.$t('formHints.signal.timeout.twelveHour')}`, value: 43200},
        {label: `${this.$t('formHints.signal.timeout.oneDay')}`, value: 86400}
      ],
    }
  },

  methods: {
    async getSymbols(page = 0, size = 10) {
      this.$q.loading.show()
      await this.$api.getSubscriptionGroupedBySymbol(this.$cookies.get('access_token'), page, size)
        .then((response) => {
          this.symbols = response.data.data
          this.symbolTotalPage = response.data.pagination.totalPages
        })
        .catch((error) => {
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },

    async getSignals(symbol, page = 0, size = 10) {
      this.$q.loading.show()
      await this.$api.getSubscriptionsBySymbols(symbol, this.$cookies.get('access_token'), page, size)
        .then((response) => {
          this.signals = response.data.data.map((signal) => {
            return {
              id: signal.id,
              marketId: signal.marketId,
              symbol: signal.symbol,
              targetType: signal.targetType,
              targetValue: signal.targetValue,
              message: signal.message,
              notificationType: signal.notificationType,
              notificationEnabled: signal.notificationEnabled,
              timeout: signal.timeout,
              lastTriggered: !!signal.lastTriggered ? moment(new Date(signal.lastTriggered * 1000)).startOf('day').fromNow() : '',
              updatedAt: signal.updatedAt
            }
          })
          this.signalTotalPage = response.data.pagination.totalPages
        })
        .catch((error) => {
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },

    async getSignalById(id) {
      this.$q.loading.show()
      await this.$api.getSubscriptionsById(id, this.$cookies.get('access_token'))
        .then((response) => {
          this.editSignal = response.data.data
        })
        .catch((error) => {
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },
    async getMarketName(marketId) {
      this.$q.loading.show()
      await this.$api.getMarket(marketId)
        .then((response) => {
          this.marketName = response.data.data.name
        })
        .catch((error) => {
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },

    async onEditSignalBtn(signalId) {
      this.editSignalId = signalId;
      await this.getSignalById(signalId)
      await this.getMarketName(this.editSignal.marketId)
      this.updateTargetValueModel = this.editSignal.targetValue
      this.updateMessageModel = this.editSignal.message
      this.updateNotificationTypeModel = this.editSignal.notificationType
      this.updateTimeoutModel = this.timeoutOptions.find(option => option.value === this.editSignal.timeout)
      this.updateNotificationEnableModel = this.editSignal.notificationEnabled
      this.updateSignalDialog = true
    },

    async getAllMarkets() {
      this.$q.loading.show()
      this.$api.getAllMarket()
        .then((response) => {
          this.marketOptions = response.data.data.map((market) => market.name)
          this.markets = response.data.data
        })
        .catch((error) => {
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },

    async onCreateNewSignal() {
      this.$q.loading.show()
      if (!this.validateSignalForm(
        this.marketModel,
        this.symbolModel,
        this.targetTypeModel,
        this.newTargetValueModel,
        this.newMessageModel,
        this.newNotificationTypeModel,
        this.newTimeoutModel)) {
        this.$q.loading.hide()
        return
      }
      const marketId = this.markets.find(market => market.name === this.marketModel).id
      this.$api.createNewSignal(
        marketId,
        this.symbolModel,
        this.targetTypeModel.value,
        this.newTargetValueModel,
        this.newMessageModel,
        this.newNotificationTypeModel,
        1,
        this.newTimeoutModel.value,
        this.$cookies.get('access_token')
      )
        .then((response) => {
          this.$q.notify({
            progress: true,
            message: this.$t('signal.createSignalSuccess'),
            type: 'positive'
          })
          this.marketModel = undefined
          this.symbolModel = undefined
          this.targetTypeModel = undefined
          this.newTargetValueModel = undefined
          this.newMessageModel = undefined
          this.newNotificationTypeModel = undefined
          this.newTimeoutModel = undefined
          this.getSymbols()
        })
        .catch((error) => {
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },

    async getAllSymbols(marketId) {
      this.$q.loading.show()
      this.$api.getAllAssets(marketId)
        .then((response) => {
          this.assets = response.data.data
          this.symbolOptions = response.data.data.map((asset) => asset.symbol)
        })
        .catch((error) => {
          console.log(error)
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },

    async onDeleteSignal() {
      this.$q.loading.show()
      await this.$api.deleteSubscription(this.deleteSignalId, this.$cookies.get('access_token'))
        .then((response) => {
          this.$q.notify({
            progress: true,
            message: this.$t('signal.deleteSignalSuccess'),
            type: 'positive'
          })
          this.getSignals(this.editSignalSymbol)
          this.getSymbols()
        })
        .catch((error) => {
          console.log(error)
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },

    async onUpdateSignal() {
      this.$q.loading.show()
      await this.$api.updateSubscription(
        this.editSignalId,
        this.updateTargetValueModel,
        this.updateMessageModel,
        this.updateNotificationTypeModel,
        this.updateNotificationEnableModel,
        this.updateTimeoutModel.value,
        this.$cookies.get('access_token'))
        .then((response) => {
          this.$q.notify({
            progress: true,
            message: this.$t('signal.updateSignalSuccess'),
            type: 'positive'
          })
          this.getSignals(this.editSignalSymbol)
          this.editSignalId = undefined
        })
        .catch((error) => {
          console.log(error)
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    },

    validateSignalForm(marketName, symbol, targetType, targetValue, message, notificationType, timeout) {
      if (!marketName || !symbol || !targetType || !targetValue || !message || !notificationType || !timeout) {
        this.$q.notify({
          progress: true,
          message: this.$t('errors.EMPTY_FILEDS'),
          type: 'negative'
        })
        return false
      }
      return true
    }
  },

  watch: {
    editSignalSymbol(newValue) {
      this.getSignals(newValue)
    },

    async symbolCurrentPage(newValue) {
      await this.getSymbols(newValue - 1)
    },

    async signalCurrentPage(newValue) {
      await this.getSignals(this.editSignalSymbol, newValue - 1)
    },

    marketModel(newValue) {
      if (newValue !== undefined) {
        const marketId = this.markets.find(market => market.name === newValue).id
        this.getAllSymbols(marketId)
      }
    },

    editSignalsDialog(newValue) {
      this.signalCurrentPage = 1
    },

    updateSignalDialog(newValue) {
      if (!newValue) {
        this.editSignalId = undefined
        this.updateTargetValueModel = undefined
        this.updateMessageModel = undefined
        this.updateNotificationTypeModel = undefined
        this.updateNotificationEnableModel = undefined
        this.updateTimeoutModel = undefined
      }
    },

    deleteSignalDialog(newValue) {
      if (!newValue) {
        this.deleteSignalId = undefined
      }
    }
  },

  async beforeMount() {
    await this.getSymbols()
    await this.getAllMarkets()
  }
}
</script>

<style scoped>

</style>
