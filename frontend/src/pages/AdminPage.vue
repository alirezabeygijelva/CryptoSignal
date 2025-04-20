<template>
  <q-page class="row justify-center q-py-lg">
    <div class="q-pa-md col-lg-8 col-md-8 col-sm-10 col-xs-12">
      <q-card>
        <q-card-section class="bg-blue-grey-9 text-white">
          <div class="text-h6">{{ $t('admin.binanceStream') }}</div>
        </q-card-section>

        <q-card-actions class="column items-start q-pa-lg">
          <div>
            <q-input outlined bottom-slots v-model="newBinanceStreamModel" :label="$t('admin.binanceStream')">
              <template v-slot:append>
                <q-btn round dense flat icon="add" @click="onAddBinanceStream" />
              </template>
            </q-input>
          </div>
          <div>
            <q-chip class="text-black" v-for="(item, index) in binanceStreams" :key="`binance-stream-${index}`" removable @remove="deleteBinanceStreamDialog = true; deleteBinanceStreamName = item.streamName" color="amber-14" text-color="white" size="16px">
              {{ item.streamName }}
            </q-chip>
            <q-dialog v-model="deleteBinanceStreamDialog" persistent>
              <q-card>
                <q-card-section class="row items-center">
                  <q-avatar icon="delete" color="negative" text-color="white" />
                  <span class="q-ml-sm">{{ $t('general.deleteDialog') }}</span>
                </q-card-section>

                <q-card-actions align="right">
                  <q-btn flat :label="$t('general.cancel')" color="primary" v-close-popup />
                  <q-btn flat :label="$t('general.delete')" color="primary" @click="onDeleteBinanceStream" v-close-popup />
                </q-card-actions>
              </q-card>
            </q-dialog>
          </div>
        </q-card-actions>
      </q-card>

      <q-card class="my-card q-mt-lg">
        <q-card-section class="bg-blue-grey-9 text-white">
          <div class="text-h6">{{ $t('admin.markets') }}</div>
        </q-card-section>

        <q-card-actions class="column items-start q-pa-lg">
          <q-btn color="positive" icon-right="add" @click="newMarketDialog = true" :label="$t('admin.newMarket')" />
          <q-dialog v-model="newMarketDialog" persistent>
            <q-card style="width: 650px; max-width: 80vw;">
              <q-card-section>
                <div class="text-h6">{{ $t('admin.newMarket') }}</div>
              </q-card-section>

              <q-card-section class="q-pt-none">
                <q-input v-model="newMarketNameModel" :label="$t('admin.marketName')" />
                <q-input v-model="newMarketDescriptionModel" :label="$t('admin.marketDescription')" />
                <q-select v-model="newMarketTypeModel" :options="marketTypeOptions" :label="$t('admin.marketType')" />
              </q-card-section>

              <q-card-actions align="right" class="text-primary">
                <q-btn flat :label="$t('general.cancel')" v-close-popup />
                <q-btn flat :label="$t('general.save')" v-close-popup @click="onAddNewMarket" />
              </q-card-actions>
            </q-card>
          </q-dialog>
        </q-card-actions>

        <q-card-section>
          <q-markup-table flat bordered>
            <thead>
            <tr>
              <th class="text-left">Id</th>
              <th class="text-left">{{ $t('admin.marketName') }}</th>
              <th class="text-left">{{ $t('admin.marketDescription') }}</th>
              <th class="text-right">{{ $t('admin.marketType') }}</th>
              <th class="text-right">{{ $t('admin.operation') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="market in markets" :key="`market-${market.id}`">
              <td class="text-left">{{ market.id }}</td>
              <td class="text-left">{{ market.name }}</td>
              <td class="text-left">{{ market.description }}</td>
              <td class="text-right">{{ market.type }}</td>
              <td class="text-right">
                <q-btn size="12px" flat dense round icon="delete" @click="deleteMarketDialog = true; deleteMarketId = market.id">
                <q-tooltip>
                  {{ $t('general.delete') }}
                </q-tooltip>
              </q-btn>
              </td>
            </tr>
            </tbody>
          </q-markup-table>
          <q-dialog v-model="deleteMarketDialog" persistent>
            <q-card>
              <q-card-section class="row items-center">
                <q-avatar icon="delete" color="negative" text-color="white" />
                <span class="q-ml-sm">{{ $t('general.deleteDialog') }}</span>
              </q-card-section>

              <q-card-actions align="right">
                <q-btn flat :label="$t('general.cancel')" color="primary" v-close-popup />
                <q-btn flat :label="$t('general.delete')" color="primary" v-close-popup @click="onDeleteMarket" />
              </q-card-actions>
            </q-card>
          </q-dialog>
        </q-card-section>

        <q-card-section>
          <div class="q-py-sm flex flex-center">
            <q-pagination
              v-model="marketCurrentPage"
              :max="marketTotalPages"
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
export default {
  name: "AdminPage",

  data() {
    return {
      newMarketDialog: false,
      deleteMarketDialog: false,
      deleteBinanceStreamDialog: false,
      marketTypeOptions: ['Cryptocurrency', 'Forex', 'Stock', 'Commodity', 'Index', 'Other'],
      newMarketTypeModel: undefined,
      newMarketNameModel: undefined,
      newMarketDescriptionModel: undefined,
      newBinanceStreamModel: undefined,
      binanceStreams: [],
      deleteBinanceStreamName: undefined,
      deleteMarketId: undefined,
      markets: [],
      marketCurrentPage: 1,
      marketTotalPages: 1
    }
  },

  methods: {
    async getAllBinanceStream() {
      this.$q.loading.show()
      await this.$api.getAllBinanceStream(this.$cookies.get('access_token'))
        .then((response) => {
          this.binanceStreams = response.data.data
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

    async onAddBinanceStream() {
      this.$q.loading.show()
      await this.$api.addNewBinanceStream(this.newBinanceStreamModel, this.$cookies.get('access_token'))
        .then((response) => {
          this.binanceStreams = response.data.data
          this.newBinanceStreamModel = undefined
          this.$q.notify({
            progress: true,
            message: this.$t('admin.addBinanceStreamSuccess'),
            type: 'positive'
          })
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

    async onDeleteBinanceStream() {
      this.$q.loading.show()
      await this.$api.deleteBinanceStream(this.deleteBinanceStreamName, this.$cookies.get('access_token'))
        .then((response) => {
          this.binanceStreams = response.data.data
          this.deleteBinanceStreamName = undefined
          this.$q.notify({
            progress: true,
            message: this.$t('admin.deleteBinanceStreamSuccess'),
            type: 'positive'
          })
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

    async getAllMarkets(page = 0, size = 10) {
      this.$q.loading.show()
      this.$api.getAllMarket(page, size)
        .then((response) => {
          this.markets = response.data.data
          this.marketTotalPages = response.data.pagination.totalPages
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

    async onAddNewMarket() {
      this.$q.loading.show()
      await this.$api.addNewMarket(this.newMarketNameModel, this.newMarketTypeModel, this.newMarketDescriptionModel, this.$cookies.get('access_token'))
        .then((response) => {
          this.getAllMarkets()
          this.newMarketNameModel = undefined
          this.newMarketTypeModel = undefined
          this.newMarketDescriptionModel = undefined
          this.$q.notify({
            progress: true,
            message: this.$t('admin.addMarketSuccess'),
            type: 'positive'
          })
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

    async onDeleteMarket() {
      this.$q.loading.show()
      await this.$api.deleteMarket(this.deleteMarketId, this.$cookies.get('access_token'))
        .then((response) => {
          this.getAllMarkets()
          this.deleteMarketId = undefined
          this.$q.notify({
            progress: true,
            message: this.$t('admin.deleteMarketSuccess'),
            type: 'positive'
          })
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
    }
  },

  watch: {
    marketCurrentPage(newValue) {
      this.getAllMarkets(newValue - 1)
    }
  },

  mounted() {
    this.getAllBinanceStream()
    this.getAllMarkets()
  }
}
</script>

<style scoped>
</style>
