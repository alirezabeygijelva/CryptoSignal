<template>
  <router-view :dir="$i18n.locale === 'fa' ? 'rtl' : 'ltr'" />
</template>

<script>
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'App',

  mounted() {
    // Set locale if missing
    if (!this.$cookies.isKey("locale")) {
      this.$cookies.set("locale", "en-US")
    }
    this.$i18n.locale = this.$cookies.get("locale")
  }
});
</script>

<style>
body {
  font-family: Vazirmatn, sans-serif;
}
</style>
