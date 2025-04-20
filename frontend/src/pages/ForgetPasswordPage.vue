<template>
  <q-page class="row justify-center q-py-lg">
    <div class="q-pa-md col-lg-8 col-md-8 col-sm-10 col-xs-12">
      <q-card flat bordered>
        <q-card-section>
          <div class="text-h6">{{ $t('forgetPassword.title') }}</div>
        </q-card-section>

        <q-card-section class="q-pt-none q-gutter-sm">
          <q-input v-model="email" :label="$t('forgetPassword.email')" standout="bg-grey-6 text-white" :hint="$t('forgetPassword.emailHint')" />
        </q-card-section>

        <q-card-actions class="q-pt-none q-px-md q-pb-md">
          <q-btn color="positive" icon-right="lock_reset" :label="$t('forgetPassword.submit')" @click="onCheckRestPassword" />
        </q-card-actions>
      </q-card>

    </div>
  </q-page>
</template>

<script>
export default {
  name: "ForgetPasswordPage",

  data() {
    return {
      email: undefined
    }
  },

  methods: {
    onCheckRestPassword() {
      this.$q.loading.show()
      this.$api.requestResetPassword(this.email)
        .then((response) => {
          this.$q.notify({
            progress: true,
            message: this.$t('notifications.successfulRequestResetPassword'),
            type: 'positive'
          })
          this.$router.push({path: "/login"})
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
