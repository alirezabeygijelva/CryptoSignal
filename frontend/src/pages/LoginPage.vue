<template>
  <q-page class="row justify-center q-py-lg">
    <div class="q-pa-md col-lg-8 col-md-8 col-sm-10 col-xs-12">

      <q-card flat bordered>
        <q-card-section>
          <div class="text-h6">{{ $t('login.title') }}</div>
        </q-card-section>

        <q-card-section class="q-pt-none q-gutter-sm">
          <q-input v-model="usernameModel" :label="$t('login.username')" standout="bg-grey-6 text-white" :hint="$t('formHints.username')" />
          <q-input v-model="passwordModel" :label="$t('login.password')" standout="bg-grey-6 text-white" :hint="$t('formHints.password')" />
        </q-card-section>

        <q-card-actions class="q-pt-none q-px-md q-pb-md">
          <q-btn color="positive" icon-right="login" :label="$t('login.submit')" @click="onLogin" />
        </q-card-actions>

        <q-separator inset/>

        <q-card-section>
          {{ $t('login.missingAccount') }}
          <router-link to="/signup">{{ $t('login.signup') }}</router-link>
        </q-card-section>
        <q-card-section class="q-pt-none">
          {{ $t('login.forgetPassword') }}
          <router-link to="/forget-password">{{ $t('login.resetPassword') }}</router-link>
        </q-card-section>
      </q-card>

    </div>
  </q-page>
</template>

<script>
export default {
  name: "LoginPage",

  data() {
    return {
      usernameModel: '',
      passwordModel: '',
    }
  },

  methods: {
    onLogin() {
      this.$q.loading.show()
      this.$api.login(this.usernameModel, this.passwordModel)
        .then((response) => {
          this.$q.notify({
            progress: true,
            message: this.$t('notifications.successfulLogin'),
            type: 'positive'
          })
          this.$cookies.set('access_token', response.data.data.accessToken, response.data.data.expiresIn)
          this.$cookies.set('refresh_token', response.data.data.refreshToken, 30 * 24 * 60 * 60)
          this.$router.go()
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
  }
}
</script>

<style scoped>
</style>
