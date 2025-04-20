<template>
  <q-page class="row justify-center q-py-lg">
    <div class="q-pa-md col-lg-8 col-md-8 col-sm-10 col-xs-12">
      <q-card class="my-card">
        <q-card-section class="bg-blue-grey-9 text-white">
          <div class="text-h6">{{ $t('home.assetsTable.title') }}</div>
        </q-card-section>

        <q-card-section>
          <q-tabs
            v-model="tab"
            dense
            class="text-grey"
            active-color="primary"
            indicator-color="primary"
            align="justify"
            narrow-indicator
          >
            <q-tab v-for="market in markets" :key="`market-${market.id}`" :name="market.id" :label="market.name"
                   no-caps/>
          </q-tabs>

          <q-separator/>

          <q-tab-panels v-model="tab" animated>
            <q-tab-panel v-for="market in markets" :key="`market-panel-${market.id}`" :name="market.id">
              <q-table
                class="text-weight-bold"
                flat bordered
                :rows="getRows"
                :columns="columns"
                row-key="name"
              >
                <template v-slot:body="props">
                  <q-tr :props="props">
                    <q-td key="logo" :props="props">
                      <q-avatar size="34px">
                        <img :src="props.row.logo">
                      </q-avatar>
                    </q-td>
                    <q-td key="symbol" :props="props">
                      {{ props.row.symbol }}
                    </q-td>
                    <q-td key="price" :props="props">
                      {{ props.row.price }}
                    </q-td>
                    <q-td key="percent" :props="props">
                      <q-badge :color="props.row.percent >= 0 ? 'positive' : 'negative'" class="text-weight-bold">
                        {{ Math.abs(props.row.percent).toLocaleString() }} %
                      </q-badge>
                    </q-td>
                    <q-td key="change" :props="props">
                      <q-badge :color="props.row.change >= 0 ? 'positive' : 'negative'" class="text-weight-bold">
                        {{ Math.abs(props.row.change).toLocaleString() }}
                      </q-badge>
                    </q-td>
                    <q-td key="volume" :props="props">
                      {{ props.row.volume }}
                    </q-td>
                    <q-td key="bid" :props="props">
                      <q-skeleton v-if="props.row.bid < 0" type="text"/>
                      <div v-else>{{ props.row.bid }}</div>
                    </q-td>
                    <q-td key="ask" :props="props">
                      <q-skeleton v-if="props.row.ask < 0" type="text"/>
                      <div v-else>{{ props.row.ask }}</div>
                    </q-td>
                  </q-tr>
                </template>
              </q-table>
            </q-tab-panel>
          </q-tab-panels>
        </q-card-section>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue';
import {mapActions, mapGetters} from "vuex";

export default defineComponent({
  name: 'IndexPage',

  data() {
    return {
      tab: undefined,
      markets: [],
      columns: [
        {name: 'logo', required: true, label: this.$t('home.assetsTable.header.logo'), align: 'center', field: 'logo'},
        {
          name: 'symbol',
          label: this.$t('home.assetsTable.header.name'),
          align: 'center',
          field: 'symbol',
          sortable: true
        },
        {
          name: 'price',
          label: this.$t('home.assetsTable.header.price'),
          field: 'price',
          align: 'center',
          sortable: true
        },
        {
          name: 'percent',
          label: this.$t('home.assetsTable.header.percent'),
          field: 'percent',
          align: 'center',
          sortable: true
        },
        {
          name: 'change',
          label: this.$t('home.assetsTable.header.change'),
          field: 'change',
          align: 'center',
          sortable: true
        },
        {
          name: 'volume',
          label: this.$t('home.assetsTable.header.volume'),
          field: 'volume',
          align: 'center',
          sortable: true
        },
        {name: 'bid', label: this.$t('home.assetsTable.header.bid'), field: 'bid', align: 'center', sortable: true},
        {name: 'ask', label: this.$t('home.assetsTable.header.ask'), field: 'ask', align: 'center', sortable: true}
      ],
      pagination: undefined,
      assets: new Map(),
    }
  },

  computed: {
    getRows() {
      let rows = []
      if (this.assets.has(this.tab)) {
        this.assets.get(this.tab)
          .forEach((value) => {
            if (this.getRealtimeAssetsMap.has(value.symbol)) {
              const assetFromStream = this.getRealtimeAssetsMap.get(value.symbol)
              rows.push({
                logo: this.$symbols[`${value.symbol}`],
                symbol: value.symbol,
                price: Number(assetFromStream.c).toLocaleString(),
                percent: Number(assetFromStream.P),
                change: Number(assetFromStream.p),
                volume: Number(assetFromStream.v).toLocaleString(),
                bid: Number(assetFromStream.b).toLocaleString(),
                ask: Number(assetFromStream.a).toLocaleString()
              })
            } else {
              rows.push({
                logo: this.$symbols[`${value.symbol}`],
                symbol: value.symbol,
                price: Number(value.closePrice).toLocaleString(),
                percent: Number(((value.closePrice - value.openPrice) / (value.openPrice)) * 100).toLocaleString(),
                change: Number(value.closePrice - value.openPrice).toLocaleString(),
                volume: Number(value.volume).toLocaleString(),
                bid: -1,
                ask: -1
              })
            }
          })
      }
      return rows
    },
    ...mapGetters(['getRealtimeAssetsMap'])
  },

  methods: {
    async getAllMarkets() {
      this.$q.loading.show()
      this.$api.getAllMarket()
        .then((response) => {
          this.markets = response.data.data
          this.tab = this.markets[0].id
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
          this.assets.set(marketId, response.data.data)

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
    ...mapActions(['connectBinanceStream'])
  },

  watch: {
    async tab(newValue) {
      await this.getAllSymbols(newValue)
    }
  },

  async beforeMount() {
    await this.getAllMarkets()
  },

  mounted() {
    this.connectBinanceStream({url: '/ws', destination: '/topic/binance/ticker'})
  }
});
</script>
