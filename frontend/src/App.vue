<template>
  <router-view />
</template>

<script setup>
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
:root { --primary: #667eea; --primary-dark: #5a67d8; --primary-light: #a3bffa; --success: #10b981; --danger: #ef4444; --warning: #f59e0b; --gray: #6b7280; --light: #f3f4f6; }
body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Microsoft YaHei', sans-serif; background: #f5f7fa; }
button { border: none; border-radius: 8px; cursor: pointer; transition: all 0.3s ease; }
button:hover { transform: translateY(-2px); opacity: 0.9; }
input, select, textarea { font-family: inherit; }
a { text-decoration: none; }
</style>