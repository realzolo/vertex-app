import { StatisticCard } from "@ant-design/pro-components";
import { Card } from "antd";

const Process = () => {
  return (
    <Card title="进程">
      <StatisticCard.Group>
        <StatisticCard
          statistic={{
            title: '进程ID',
            value: 5,
            status: 'default',
          }}
        />
        <StatisticCard
          statistic={{
            title: '运行时间',
            value: 3,
            status: 'processing',
          }}
        />
        <StatisticCard
          statistic={{
            title: '进程CPU使用率',
            value: 2,
            status: 'error',
          }}
        />
        <StatisticCard
          layout={"center"}
          statistic={{
            title: '系统CPU使用率',
            value: '-',
            status: 'success',
          }}
        />
        <StatisticCard
          statistic={{
            title: 'CPU核心数',
            value: '8',
            status: 'success',
          }}
        />
      </StatisticCard.Group>
    </Card>
  )
}
export default Process;
