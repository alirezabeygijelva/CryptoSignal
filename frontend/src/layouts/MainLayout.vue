<template>
  <q-layout view="hHh lpR fff">

    <q-header class="q-mt-md row wrap justify-center items-start content-start" style="background: transparent">
      <q-toolbar class="col-xs-11 col-sm-10 col-md-8 col-lg-8 row justify-around bg-blue-grey-5"
                 style="border-radius: 15px;">

        <div class="gt-xs">
          <q-btn v-show="isAuthenticated && (isUser || isAdmin)" to="/" :class="{'bottom-border' : this.$route.path === '/'}" :label="$t('header.home')" color="white"
                 icon="home" flat stack no-caps/>
          <q-btn v-show="isAuthenticated && (isUser || isAdmin)" to="/signals" :class="{'bottom-border' : this.$route.path === '/signals'}" :label="$t('header.signal')"
                 color="white" icon="contactless" flat stack no-caps/>
          <q-btn v-show="isAuthenticated && isAdmin" to="/admin" :class="{'bottom-border' : this.$route.path === '/admin'}" :label="$t('header.admin')"
                 color="white" icon="admin_panel_settings" flat stack no-caps/>
          <q-btn v-show="!isAuthenticated" to="/login" :class="{'bottom-border' : this.$route.path === '/login'}" :label="$t('header.login')"
                 color="white" icon="login" flat stack no-caps/>
          <q-btn v-show="!isAuthenticated" to="/signup" :class="{'bottom-border' : this.$route.path === '/signup'}" :label="$t('header.signup')"
                 color="white" icon="person_add" flat stack no-caps/>
        </div>

        <div class="xs">
          <q-btn-dropdown dropdown-icon="menu">
            <q-list>
              <q-item v-show="isAuthenticated && (isUser || isAdmin)" to="/" clickable v-close-popup>
                <q-item-section class="items-center">
                  <q-item-label :class="{'text-bold' : this.$route.path === '/'}">{{ $t('header.home') }}</q-item-label>
                </q-item-section>
              </q-item>

              <q-item v-show="isAuthenticated && (isUser || isAdmin)" to="/signals" clickable v-close-popup>
                <q-item-section class="items-center">
                  <q-item-label :class="{'text-bold' : this.$route.path === '/signals'}">{{ $t('header.signal') }}</q-item-label>
                </q-item-section>
              </q-item>

              <q-item v-show="isAuthenticated && isAdmin" to="/admin" clickable v-close-popup>
                <q-item-section class="items-center">
                  <q-item-label :class="{'text-bold' : this.$route.path === '/admin'}"> {{ $t('header.admin') }}</q-item-label>
                </q-item-section>
              </q-item>

              <q-item v-show="!isAuthenticated" to="/login" clickable v-close-popup>
                <q-item-section class="items-center">
                  <q-item-label :class="{'text-bold' : this.$route.path === '/login'}">{{ $t('header.login') }}</q-item-label>
                </q-item-section>
              </q-item>

              <q-item v-show="!isAuthenticated" to="/signup" clickable v-close-popup>
                <q-item-section class="items-center">
                  <q-item-label :class="{'text-bold' : this.$route.path === '/signup'}">{{ $t('header.signup') }}</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>
        </div>

        <q-space/>

        <div class="row">
          <q-btn-dropdown
            flat
            color="white"
            push
            no-caps
            dense
            class="q-ml-xs"
            rounded
            v-show="isAuthenticated"
          >
            <template v-slot:label>
              <div class="row items-center no-wrap">
                <q-icon left name="account_circle"/>
                <div class="text-center gt-xs">
                  {{ `${firstName} ${lastName}`.substring(0, 6) }}...
                </div>
              </div>
            </template>



            <q-list>
              <q-item @click="this.$router.push('/settings')" clickable v-close-popup dense>
                <q-item-section avatar>
                  <q-avatar icon="settings" text-color="black" dense/>
                </q-item-section>
                <q-item-section>
                  <q-item-label>{{ $t('header.setting') }}</q-item-label>
                </q-item-section>
              </q-item>

              <q-item clickable v-close-popup dense @click="onLogout">
                <q-item-section avatar>
                  <q-avatar icon="logout" text-color="black"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label>{{ $t('header.logout') }}</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>

          <q-btn-dropdown color="primary" dropdown-icon="language" dense round>
            <q-list dense>
              <q-item clickable v-close-popup v-for="(l, index) in localeOptions" :key="'local-option-' + index"
                      @click="onLocale(l)">
                <q-item-section>
                  <q-item-label :class="{'text-weight-bolder': this.$i18n.locale === l.value}">{{
                      l.label
                    }}
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>
        </div>


      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>

    <q-footer class="row justify-evenly q-py-md" style="background: transparent; z-index: initial">
      <q-toolbar class="col-xs-11 col-sm-10 col-md-8 col-lg-8 bg-blue-grey-9" style="border-radius: 15px;">
        <div class="row flex  q-pa-md justify-between">
          <div class="column flex col-xs-12 col-md-4 items-start q-mb-md" style="order: 1">
            <a target="_blank" href="https://www.example.com">
              <img alt="logo" width="150" height="45" src="../assets/logo/logo.png">
            </a>
            <p class="q-mt-xs">{{ $t('footer.description') }}</p>
          </div>
          <div class="column flex col-xs-12 col-md-4 items-center q-mb-md" style="order: 2">
            <div class="text-weight-bolder">
              <p>{{ $t('footer.contactUs') }}</p>
            </div>
            <span>{{ $t('footer.supportPhone') }}</span>
          </div>
          <div class="column flex col-xs-12 col-md-4 items-center q-mb-md" style="order: 3">
            <div class="text-weight-bolder">
              <p>{{ $t('footer.socials') }}</p>
            </div>
            <div>
              <q-list class="q-gutter-xs" dense>
                <q-item clickable v-ripple href="https://www.linkedin.com/" target="_blank">
                  <q-item-section avatar>
                    <q-avatar size="md">
                      <img src="../assets/icons/social/linkedin.png">
                    </q-avatar>
                  </q-item-section>
                  <q-item-section>{{ $t('footer.linkedin') }}</q-item-section>
                </q-item>

                <q-item clickable v-ripple href="https://instagram.com" target="_blank">
                  <q-item-section avatar>
                    <q-avatar size="md">
                      <img src="../assets/icons/social/instagram.png">
                    </q-avatar>
                  </q-item-section>
                  <q-item-section>{{ $t('footer.instagram') }}</q-item-section>
                </q-item>

                <q-item clickable v-ripple href="https://t.me/" target="_blank">
                  <q-item-section avatar>
                    <q-avatar size="md">
                      <img src="../assets/icons/social/telegram.png">
                    </q-avatar>
                  </q-item-section>
                  <q-item-section>{{ $t('footer.telegram') }}</q-item-section>
                </q-item>
              </q-list>
            </div>
          </div>
        </div>
      </q-toolbar>
    </q-footer>


  </q-layout>
</template>

<script>
import {defineComponent, ref} from 'vue'
import {mapGetters} from "vuex";

export default defineComponent({
  name: 'MainLayout',

  components: {},

  data() {
    return {
      locale: null,
      localeOptions: [
        {value: 'en-US', label: 'English'},
        {value: 'fa', label: 'فارسی'}
      ]
    }
  },

  methods: {
    onLocale(l) {
      this.$i18n.locale = l.value
      this.$cookies.set("locale", l.value, -1)
      this.$router.go()
    },
    onLogout() {
      this.$cookies.remove("access_token")
      this.$cookies.remove("refresh_token")
      this.$store.dispatch("logout")
      this.$router.go()
    }
  },

  computed: {
    isAuthenticated() {
      return !!this.$cookies.get("access_token")
    },
    ...mapGetters(['firstName', 'lastName', 'isAdmin', 'isUser'])
  }
})
</script>

<style>
.bottom-border {
  border-bottom: 4px solid #1976d2;
  border-radius: 0;
}
</style>
