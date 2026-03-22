<template>
  <div class="dashboard-container">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="统计时间">
          <el-select v-model="filterForm.timeType" placeholder="请选择" @change="handleTimeTypeChange">
            <el-option label="今日" value="today"></el-option>
            <el-option label="本月" value="month"></el-option>
            <el-option label="本年" value="year"></el-option>
            <el-option label="自定义" value="custom"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="filterForm.timeType === 'custom'">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            @change="handleDateChange"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadStatistics">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-header">
            <i class="el-icon-document"></i>
            <span>总单数</span>
          </div>
          <div class="stat-value">{{ statistics.totalOrders }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-header">
            <i class="el-icon-goods"></i>
            <span>总重量(kg)</span>
          </div>
          <div class="stat-value">{{ statistics.totalWeight }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-header">
            <i class="el-icon-money"></i>
            <span>总金额(元)</span>
          </div>
          <div class="stat-value">{{ statistics.totalAmount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-header">
            <i class="el-icon-price"></i>
            <span>平均单价</span>
          </div>
          <div class="stat-value">{{ statistics.avgPrice }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="chart-card">
      <div slot="header">
        <span>每日收购趋势</span>
      </div>
      <div ref="trendChart" class="trend-chart"></div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import moment from 'moment'

export default {
  name: 'Dashboard',
  data() {
    return {
      filterForm: {
        timeType: 'today',
        dateRange: []
      },
      statistics: {
        totalOrders: 0,
        totalWeight: 0,
        totalAmount: 0,
        avgPrice: 0
      },
      trendChart: null,
      trendData: {
        dates: [],
        weights: [],
        amounts: []
      }
    }
  },
  mounted() {
    this.initDateRange()
    this.loadStatistics()
    this.initTrendChart()
  },
  methods: {
    initDateRange() {
      const today = moment().format('YYYY-MM-DD')
      this.filterForm.dateRange = [today, today]
    },
    handleTimeTypeChange(value) {
      const today = moment()
      switch (value) {
        case 'today':
          this.filterForm.dateRange = [today.format('YYYY-MM-DD'), today.format('YYYY-MM-DD')]
          break
        case 'month':
          this.filterForm.dateRange = [
            today.startOf('month').format('YYYY-MM-DD'),
            today.endOf('month').format('YYYY-MM-DD')
          ]
          break
        case 'year':
          this.filterForm.dateRange = [
            today.startOf('year').format('YYYY-MM-DD'),
            today.endOf('year').format('YYYY-MM-DD')
          ]
          break
        case 'custom':
          // 保持当前选择的日期范围
          break
      }
      if (value !== 'custom') {
        this.loadStatistics()
      }
    },
    handleDateChange() {
      if (this.filterForm.dateRange && this.filterForm.dateRange.length === 2) {
        this.loadStatistics()
      }
    },
    loadStatistics() {
      // 这里应该调用后端API获取统计数据
      // 模拟数据
      this.statistics = {
        totalOrders: 25,
        totalWeight: 12500.50,
        totalAmount: 37501.50,
        avgPrice: 3.00
      }

      // 加载趋势图数据
      this.loadTrendData()
    },
    loadTrendData() {
      // 模拟趋势数据
      const dates = []
      const weights = []
      const amounts = []

      for (let i = 6; i >= 0; i--) {
        const date = moment().subtract(i, 'days')
        dates.push(date.format('MM-DD'))
        weights.push(Math.random() * 3000 + 1000)
        amounts.push(Math.random() * 10000 + 3000)
      }

      this.trendData = { dates, weights, amounts }
      this.updateTrendChart()
    },
    initTrendChart() {
      this.trendChart = echarts.init(this.$refs.trendChart)
      this.updateTrendChart()
    },
    updateTrendChart() {
      if (!this.trendChart) return

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data: ['收购重量', '收购金额']
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: this.trendData.dates
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '重量(kg)',
            position: 'left'
          },
          {
            type: 'value',
            name: '金额(元)',
            position: 'right'
          }
        ],
        series: [
          {
            name: '收购重量',
            type: 'line',
            smooth: true,
            areaStyle: {
              opacity: 0.3
            },
            data: this.trendData.weights
          },
          {
            name: '收购金额',
            type: 'line',
            yAxisIndex: 1,
            smooth: true,
            areaStyle: {
              opacity: 0.3
            },
            data: this.trendData.amounts
          }
        ]
      }

      this.trendChart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  .filter-card {
    margin-bottom: 20px;
  }

  .statistics-row {
    margin-bottom: 20px;

    .stat-card {
      .stat-header {
        display: flex;
        align-items: center;
        color: #909399;
        font-size: 14px;

        i {
          margin-right: 8px;
          font-size: 20px;
        }
      }

      .stat-value {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
        margin-top: 10px;
      }
    }
  }

  .chart-card {
    .trend-chart {
      height: 400px;
    }
  }
}
</style>