<template>
  <div class="transaction-entry-container">
    <el-card class="customer-card" v-for="(customer, customerIndex) in customers" :key="customerIndex">
      <div slot="header" class="customer-header">
        <span>客户: {{ customer.name || '新客户' }}</span>
        <el-button type="danger" size="small" @click="removeCustomer(customerIndex)" v-if="customers.length > 1">
          关闭订单
        </el-button>
      </div>

      <el-form :model="customer" label-width="100px" class="customer-form">
        <el-form-item label="客户名称">
          <el-input v-model="customer.name" placeholder="请输入客户名称"></el-input>
        </el-form-item>
        <el-form-item label="订单日期">
          <el-date-picker
            v-model="customer.orderDate"
            type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="customer.remark" type="textarea" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>

      <div class="vehicle-list">
        <div class="vehicle-header">
          <span>粮食明细</span>
          <el-button type="primary" size="small" @click="addVehicle(customerIndex)">
            <i class="el-icon-plus"></i> 添加车辆
          </el-button>
        </div>

        <el-table :data="customer.vehicles" border style="width: 100%" class="vehicle-table">
          <el-table-column type="index" label="序号" width="60"></el-table-column>
          <el-table-column label="运输工具" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.vehicleNo" placeholder="车牌号"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="农作物类型" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.cropType" placeholder="请选择" style="width: 100%">
                <el-option label="玉米" value="玉米"></el-option>
                <el-option label="小麦" value="小麦"></el-option>
                <el-option label="油菜籽" value="油菜籽"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="单价(元/kg)" width="120">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.price" type="number" placeholder="单价" @input="calculateRow(scope.row)"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="毛重(kg)" width="120">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.grossWeight" type="number" placeholder="毛重" @input="calculateRow(scope.row)"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="皮重(kg)" width="120">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.tareWeight" type="number" placeholder="皮重" @input="calculateRow(scope.row)"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="净重(kg)" width="120">
            <template slot-scope="scope">
              <span class="net-weight">{{ scope.row.netWeight || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="金额(元)" width="120">
            <template slot-scope="scope">
              <span class="amount">{{ scope.row.amount || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="备注"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeVehicle(customerIndex, scope.$index)"></el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="vehicle-summary">
          <div class="summary-item">
            <span>合计车数: {{ customer.vehicles.length }} 车</span>
          </div>
          <div class="summary-item">
            <span>总净重: {{ calculateTotalNetWeight(customer) }} kg</span>
          </div>
          <div class="summary-item">
            <span>总金额: {{ calculateTotalAmount(customer) }} 元</span>
          </div>
          <div class="summary-item">
            <span>平均单价: {{ calculateAvgPrice(customer) }} 元/kg</span>
          </div>
        </div>
      </div>

      <div class="form-actions">
        <el-button type="primary" @click="saveOrder(customerIndex)" :loading="customer.saving">保存单据</el-button>
        <el-button @click="printOrder(customerIndex)">打印单据</el-button>
      </div>
    </el-card>

    <div class="add-customer-container">
      <el-button type="success" icon="el-icon-plus" @click="addCustomer">添加客户订单</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TransactionEntry',
  data() {
    return {
      customers: [
        {
          name: '',
          orderDate: new Date(),
          remark: '',
          vehicles: [],
          saving: false
        }
      ]
    }
  },
  methods: {
    addCustomer() {
      this.customers.push({
        name: '',
        orderDate: new Date(),
        remark: '',
        vehicles: [],
        saving: false
      })
    },
    removeCustomer(index) {
      this.$confirm('确认关闭此客户订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.customers.splice(index, 1)
      }).catch(() => {})
    },
    addVehicle(customerIndex) {
      this.customers[customerIndex].vehicles.push({
        vehicleNo: '',
        cropType: '玉米',
        price: this.getLastPrice('玉米'),
        grossWeight: null,
        tareWeight: null,
        netWeight: null,
        amount: null,
        remark: ''
      })
    },
    removeVehicle(customerIndex, vehicleIndex) {
      this.customers[customerIndex].vehicles.splice(vehicleIndex, 1)
    },
    getLastPrice(cropType) {
      // 这里应该从后端获取最新价格，现在用模拟数据
      const prices = {
        '玉米': 3.0,
        '小麦': 2.8,
        '油菜籽': 5.5
      }
      return prices[cropType] || 3.0
    },
    calculateRow(row) {
      // 计算净重
      if (row.grossWeight && row.tareWeight) {
        row.netWeight = row.grossWeight - row.tareWeight
      }

      // 计算金额
      if (row.netWeight && row.price) {
        row.amount = row.netWeight * row.price
      }
    },
    calculateTotalNetWeight(customer) {
      return customer.vehicles.reduce((total, vehicle) => {
        return total + (vehicle.netWeight || 0)
      }, 0)
    },
    calculateTotalAmount(customer) {
      return customer.vehicles.reduce((total, vehicle) => {
        return total + (vehicle.amount || 0)
      }, 0)
    },
    calculateAvgPrice(customer) {
      const totalWeight = this.calculateTotalNetWeight(customer)
      const totalAmount = this.calculateTotalAmount(customer)
      return totalWeight > 0 ? (totalAmount / totalWeight).toFixed(2) : 0
    },
    saveOrder(customerIndex) {
      const customer = this.customers[customerIndex]

      if (!customer.name) {
        this.$message.error('请输入客户名称')
        return
      }

      if (customer.vehicles.length === 0) {
        this.$message.error('请至少添加一辆车')
        return
      }

      // 验证车辆信息
      for (let i = 0; i < customer.vehicles.length; i++) {
        const vehicle = customer.vehicles[i]
        if (!vehicle.vehicleNo) {
          this.$message.error(`第${i+1}辆车请输入车牌号`)
          return
        }
        if (!vehicle.grossWeight || !vehicle.tareWeight) {
          this.$message.error(`第${i+1}辆车请输入毛重磅和皮重`)
          return
        }
      }

      customer.saving = true

      // 模拟保存到后端
      setTimeout(() => {
        this.$message.success('订单保存成功')
        customer.saving = false

        // 保存价格到缓存
        customer.vehicles.forEach(vehicle => {
          this.cachePrice(vehicle.cropType, vehicle.price)
        })
      }, 1000)
    },
    cachePrice(cropType, price) {
      // 缓存价格到localStorage
      const priceCache = JSON.parse(localStorage.getItem('priceCache') || '{}')
      priceCache[cropType] = price
      localStorage.setItem('priceCache', JSON.stringify(priceCache))
    },
    printOrder(customerIndex) {
      this.$message.info('打印功能开发中...')
      // 这里应该实现打印功能
    }
  }
}
</script>

<style lang="scss" scoped>
.transaction-entry-container {
  .customer-card {
    margin-bottom: 20px;

    .customer-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .customer-form {
      margin-bottom: 20px;
    }

    .vehicle-list {
      .vehicle-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 1px solid #e6e6e6;
      }

      .vehicle-table {
        margin-bottom: 15px;

        .net-weight, .amount {
          font-weight: bold;
          color: #67c23a;
        }
      }

      .vehicle-summary {
        display: flex;
        justify-content: space-around;
        padding: 15px;
        background: #f5f7fa;
        border-radius: 4px;
        margin-bottom: 20px;

        .summary-item {
          font-size: 14px;
          color: #606266;

          span {
            font-weight: bold;
          }
        }
      }
    }

    .form-actions {
      text-align: center;
      padding-top: 20px;
      border-top: 1px solid #e6e6e6;
    }
  }

  .add-customer-container {
    text-align: center;
    margin-top: 20px;
  }
}
</style>