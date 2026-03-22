<template>
  <div class="data-details-container">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="filterForm.customerName" placeholder="请输入客户名称"></el-input>
        </el-form-item>
        <el-form-item label="最小金额">
          <el-input v-model.number="filterForm.minAmount" type="number" placeholder="最小金额"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadOrders">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="summary-info" slot="header">
        <span>订单列表</span>
        <div class="summary-numbers">
          <span>总计单数: {{ totalOrders }}</span>
          <span>总计重量: {{ totalWeight }} kg</span>
          <span>总计金额: {{ totalAmount }} 元</span>
        </div>
      </div>

      <el-table :data="orders" border style="width: 100%" v-loading="loading">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-table :data="props.row.details" border style="width: 100%" class="detail-table">
              <el-table-column type="index" label="序号" width="60"></el-table-column>
              <el-table-column prop="vehicleNo" label="车牌号" width="120"></el-table-column>
              <el-table-column prop="cropType.cropName" label="农作物类型" width="120"></el-table-column>
              <el-table-column prop="grossWeight" label="毛重(kg)" width="100"></el-table-column>
              <el-table-column prop="tareWeight" label="皮重(kg)" width="100"></el-table-column>
              <el-table-column prop="netWeight" label="净重(kg)" width="100"></el-table-column>
              <el-table-column prop="pricePerKg" label="单价(元/kg)" width="110"></el-table-column>
              <el-table-column prop="amount" label="金额(元)" width="100"></el-table-column>
              <el-table-column prop="remark" label="备注"></el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单编号" width="150"></el-table-column>
        <el-table-column prop="customer.customerName" label="客户名称" width="150"></el-table-column>
        <el-table-column prop="orderDate" label="订单日期" width="120"></el-table-column>
        <el-table-column prop="totalVehicles" label="车数" width="60"></el-table-column>
        <el-table-column prop="totalNetWeight" label="总净重(kg)" width="120"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额(元)" width="120"></el-table-column>
        <el-table-column prop="avgPrice" label="平均单价" width="100"></el-table-column>
        <el-table-column label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
              {{ scope.row.status === 'active' ? '有效' : '已关闭' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="editOrder(scope.row)">修改</el-button>
            <el-button type="danger" size="mini" @click="deleteOrder(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px; text-align: right"
      ></el-pagination>
    </el-card>

    <!-- 订单编辑对话框 -->
    <el-dialog
      title="编辑订单"
      :visible.sync="editDialogVisible"
      width="80%"
      top="5vh"
      :close-on-click-modal="false"
    >
      <el-form :model="editingOrder" label-width="100px" v-if="editingOrder">
        <el-form-item label="客户名称">
          <el-input v-model="editingOrder.customer.customerName"></el-input>
        </el-form-item>
        <el-form-item label="订单日期">
          <el-date-picker
            v-model="editingOrder.orderDate"
            type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editingOrder.remark" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveOrder">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'DataDetails',
  data() {
    return {
      filterForm: {
        dateRange: [],
        customerName: '',
        minAmount: null
      },
      orders: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      totalOrders: 0,
      totalWeight: 0,
      totalAmount: 0,
      editDialogVisible: false,
      editingOrder: null
    }
  },
  mounted() {
    this.initDateRange()
    this.loadOrders()
  },
  methods: {
    initDateRange() {
      const today = new Date()
      const lastWeek = new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000)
      this.filterForm.dateRange = [
        lastWeek.toISOString().split('T')[0],
        today.toISOString().split('T')[0]
      ]
    },
    loadOrders() {
      this.loading = true

      // 模拟数据
      setTimeout(() => {
        const mockOrders = []
        for (let i = 0; i < 20; i++) {
          mockOrders.push({
            id: i + 1,
            orderNo: `ORD${String(i + 1).padStart(5, '0')}`,
            customer: {
              id: 1,
              customerName: `客户${i + 1}`
            },
            orderDate: `2024-01-${String(i + 1).padStart(2, '0')}`,
            totalVehicles: Math.floor(Math.random() * 5) + 1,
            totalNetWeight: (Math.random() * 5000 + 1000).toFixed(2),
            totalAmount: (Math.random() * 20000 + 5000).toFixed(2),
            avgPrice: (Math.random() * 2 + 2.5).toFixed(2),
            status: i < 15 ? 'active' : 'closed',
            remark: '备注信息',
            details: this.generateMockDetails()
          })
        }

        this.orders = mockOrders.slice(
          (this.currentPage - 1) * this.pageSize,
          this.currentPage * this.pageSize
        )
        this.total = mockOrders.length

        // 计算总计
        this.calculateTotals(mockOrders)

        this.loading = false
      }, 500)
    },
    generateMockDetails() {
      const details = []
      const vehicleCount = Math.floor(Math.random() * 3) + 1

      for (let i = 0; i < vehicleCount; i++) {
        details.push({
          id: i + 1,
          vehicleNo: `京A${String(Math.floor(Math.random() * 10000)).padStart(5, '0')}`,
          cropType: { cropName: ['玉米', '小麦', '油菜籽'][Math.floor(Math.random() * 3)] },
          grossWeight: (Math.random() * 5000 + 5000).toFixed(2),
          tareWeight: (Math.random() * 1000 + 500).toFixed(2),
          netWeight: (Math.random() * 4000 + 4000).toFixed(2),
          pricePerKg: (Math.random() * 2 + 2.5).toFixed(2),
          amount: (Math.random() * 15000 + 10000).toFixed(2),
          remark: ''
        })
      }

      return details
    },
    calculateTotals(allOrders) {
      this.totalOrders = allOrders.length
      this.totalWeight = allOrders.reduce((sum, order) => sum + parseFloat(order.totalNetWeight), 0).toFixed(2)
      this.totalAmount = allOrders.reduce((sum, order) => sum + parseFloat(order.totalAmount), 0).toFixed(2)
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.loadOrders()
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.loadOrders()
    },
    resetFilter() {
      this.filterForm = {
        dateRange: [],
        customerName: '',
        minAmount: null
      }
      this.initDateRange()
      this.loadOrders()
    },
    editOrder(order) {
      this.editingOrder = JSON.parse(JSON.stringify(order))
      this.editDialogVisible = true
    },
    saveOrder() {
      this.$message.success('订单保存成功')
      this.editDialogVisible = false
      this.loadOrders()
    },
    deleteOrder(order) {
      this.$confirm('确认删除此订单吗？此操作不可恢复', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('订单删除成功')
        this.loadOrders()
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.data-details-container {
  .filter-card {
    margin-bottom: 20px;
  }

  .table-card {
    .summary-info {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .summary-numbers {
        span {
          margin-left: 20px;
          font-size: 14px;
          color: #606266;
        }
      }
    }

    .detail-table {
      background: #f5f7fa;
    }
  }
}
</style>