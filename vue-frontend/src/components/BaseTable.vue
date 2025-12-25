<template>
  <div class="base-table-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ title }}</span>
          <el-button type="primary" @click="openAddDialog">
            <el-icon><Plus /></el-icon> 新增
          </el-button>
        </div>
      </template>

      <div class="toolbar">
        <el-input
          v-model="searchKeyword"
          :placeholder="searchPlaceholder"
          class="search-input"
          clearable
          @clear="fetchData"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon> 查询
            </el-button>
          </template>
        </el-input>
      </div>

      <el-table
        :data="tableData"
        stripe
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          v-for="col in columns"
          :key="col.prop"
          :prop="col.prop"
          :label="col.label"
          :width="col.width"
          :formatter="col.formatter"
        />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-popconfirm
              title="确定删除吗？"
              @confirm="handleDelete(scope.row)"
            >
              <template #reference>
                <el-button type="danger" size="small" circle>
                  <el-icon><Delete /></el-icon>
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Add/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="'新增' + title" width="500px">
      <el-form :model="formData" label-width="80px" ref="formRef">
        <el-form-item
          v-for="item in formItems"
          :key="item.prop"
          :label="item.label"
          :prop="item.prop"
          :rules="[
            { required: true, message: '请输入' + item.label, trigger: 'blur' },
          ]"
        >
          <el-input v-if="item.type === 'text'" v-model="formData[item.prop]" />
          <el-input-number
            v-else-if="item.type === 'number'"
            v-model="formData[item.prop]"
            :step="item.step || 1"
            style="width: 100%"
          />
          <el-select
            v-else-if="item.type === 'select'"
            v-model="formData[item.prop]"
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option
              v-for="opt in item.options"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from "vue";
import request from "../api/request";
import { ElMessage } from "element-plus";

const props = defineProps({
  title: String,
  entity: String, // e.g., 'customers'
  columns: Array,
  formItems: Array,
  searchPlaceholder: String,
  searchParam: {
    type: String,
    default: "name",
  },
});

const tableData = ref([]);
const loading = ref(false);
const searchKeyword = ref("");
const dialogVisible = ref(false);
const formData = reactive({});
const formRef = ref(null);

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await request.get(`/${props.entity}`);
    tableData.value = res;
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const handleSearch = async () => {
  if (!searchKeyword.value) {
    fetchData();
    return;
  }
  loading.value = true;
  try {
    const res = await request.get(`/${props.entity}/search`, {
      params: { [props.searchParam]: searchKeyword.value },
    });
    tableData.value = res;
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const openAddDialog = () => {
  // Reset form
  props.formItems.forEach((item) => {
    formData[item.prop] =
      item.defaultValue !== undefined ? item.defaultValue : "";
  });
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await request.post(`/${props.entity}`, formData);
        ElMessage.success("保存成功");
        dialogVisible.value = false;
        fetchData();
      } catch (error) {
        console.error(error);
      }
    }
  });
};

const handleDelete = async (row) => {
  try {
    await request.delete(`/${props.entity}/${row.id}`);
    ElMessage.success("删除成功");
    fetchData();
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => {
  fetchData();
});

// Expose fetchData so parent can call it if needed
defineExpose({ fetchData });
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.toolbar {
  margin-bottom: 20px;
  width: 300px;
}
</style>
