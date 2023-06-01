<script>
import render from './render/render'

const _layouts = {
  // eslint-disable-next-line func-names
  'ts-table': function (h, currentData, syncData, syncDataKey) {
    return (
      <render
        data={currentData}
        syncData={syncData}
        syncDataKey={syncDataKey}
        onInput={ event => { this.$set(syncData, syncDataKey, event) }}>
      </render>
    )
  }
}

export default {
  name: 'TsRender',
  props: [
    'type',
    'currentData',
    'syncData',
    'syncDataKey'
  ],
  render(h) {
    const _layout = _layouts['ts-table']
    if (_layout && this.currentData != null) {
      return _layout.call(this, h, this.currentData, this.syncData, this.syncDataKey)
    }
    return null
  }
}
</script>