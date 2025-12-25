<template>
  <div class="base-table">
    <el-card shadow="never">
      <div class="table-header">
        <el-input
          v-model="searchQuery"
          placeholder="搜索..."
          prefix-icon="Search"
          style="width: 300px"
          clearable
          @input="handleSearch"
        />
        <el-button type="primary" icon="Plus" @click="handleAdd">新增记录</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" style="width: 100%; margin-top: 20px" stripe>
        <el-table-column
          v-for="col in columns"
          :key="col.prop"
          :prop="col.prop"
          :label="col.label"
          :width="col.width"
        >
          <template #default="scope">
            <span v-if="col.type === 'price'" style="color: #67C23A; font-weight: bold;">
              ¥{{ scope.row[col.prop] }}
            </span>
            <el-tag v-else-if="col.type === 'stock'" :type="scope.row[col.prop] < 10 ? 'danger' : 'success'">
              {{ scope.row[col.prop] }}
            </el-tag>
            <span v-else>{{ scope.row[col.prop] }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="180" align="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" label-width="80px">
        <el-form-item
          v-for="field in formFields"
          :key="field.prop"
          :label="field.label"
          :prop="field.prop"
          :rules="[{ required: true, message: '请输入' + field.label, trigger: 'blur' }]"
        >
          <el-input v-if="!field.type || field.type === 'text'" v-model="formData[field.prop]" />
          <el-input-number v-else-if="field.type === 'number'" v-model="formData[field.prop]" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import request from '@/api/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  type: {
    type: String,
    required: true
  }
})

const loading = ref(false)
const list = ref([])
const searchQuery = ref('')
const dialogVisible = ref(false)
const formData = ref({})
const formRef = ref(null)
const isEdit = ref(false)

const configs = {
  fruits: {
    columns: [
      { prop: 'id', label: 'ID', width: 80 },
      { prop: 'name', label: '名称' },
      { prop: 'price', label: '单价', type: 'price' },
      { prop: 'unit', label: '单位' },
      { prop: 'stock', label: '库存', type: 'stock' }
    ],
    fields: [
      { prop: 'name', label: '名称' },
      { prop: 'price', label: '单价', type: 'number' },
      { prop: 'unit', label: '单位' },
      { prop: 'stock', label: '库存', type: 'number' }
    ]
  },
  employees: {
    columns: [
      { prop: 'id', label: 'ID', width: 80 },
      { prop: 'name', label: '姓名' },
      { prop: 'phone', label: '电话' },
      { prop: 'position', label: '职位' }
    ],
    fields: [
      { prop: 'name', label: '姓名' },
      { prop: 'phone', label: '电话' },
      { prop: 'position', label: '职位' }
    ]
  },
  customers: {
    columns: [
      { prop: 'id', label: 'ID', width: 80 },
      { prop: 'name', label: '姓名' },
      { prop: 'phone', label: '电话' },
      { prop: 'membershipId', label: '会员号' }
    ],
    fields: [
      { prop: 'name', label: '姓名' },
      { prop: 'phone', label: '电话' },
      { prop: 'membershipId', label: '会员号' }
    ]
  },
  suppliers: {
    columns: [
      { prop: 'id', label: 'ID', width: 80 },
      { prop: 'name', label: '名称' },
      { prop: 'phone', label: '电话' },
      { prop: 'address', label: '地址' }
    ],
    fields: [
      { prop: 'name', label: '名称' },
      { prop: 'phone', label: '电话' },
      { prop: 'address', label: '地址' }
    ]
  }
}

const columns = computed(() => configs[props.type]?.columns || [])
const formFields = computed(() => configs[props.type]?.fields || [])
const dialogTitle = computed(() => (isEdit.value ? '编辑' : '新增'))

const tableData = computed(() => {
  if (!searchQuery.value) return list.value
  const q = searchQuery.value.toLowerCase()
  return list.value.filter(item => 
    Object.values(item).some(val => String(val).toLowerCase().includes(q))
  )
})

const fetchData = async () => {
  loading.value = true
  try {
    list.value = await request.get(`/${props.type}`)
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  if (!searchQuery.value) {
    fetchData()
    return
  }
  // Optional: Server side search if needed, but client side is fine for small data
  // list.value = await request.get(`/${props.type}/search`, { params: { name: searchQuery.value } })
}

const handleAdd = () => {
  isEdit.value = false
  formData.value = {}
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  formData.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该记录吗?', '提示', {
    type: 'warning'
  }).then(async () => {
    await request.delete(`/${props.type}/${row.id}`)
    ElMessage.success('删除成功')
    fetchData()
  })
}

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          // Some APIs might use PUT, some POST. Assuming standard REST
          // But wait, the backend controller uses POST for create, but what about update?
          // The backend `saveCustomer` handles both create and update if ID exists.
          // So POST is fine if ID is present in body.
          await request.post(`/${props.type}`, formData.value)
        } else {
          await request.post(`/${props.type}`, formData.value)
        }
        ElMessage.success('保存成功')
        dialogVisible.value = false
        fetchData()
      } catch (e) {
        // Error handled by interceptor
      }
    }
  })
}

watch(() => props.type, () => {
  searchQuery.value = ''
  fetchData()
}, { immediate: true })

</script>

<style scoped>
.table-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}
</style>
