import { FC, useEffect, useState } from "react";
import { ProCard } from "@ant-design/pro-components";
import { measureTextWidth, Pie } from '@ant-design/plots';

interface ChartData {
  type: string;
  value: number;
}

interface Props {
  data: Record<string, string>[] | undefined;
}

/**
 * 忽略统计的 Redis 命令
 */
const ignoredRedisCommands = [
  'get',
  'info',
  'hello',
  'dbSize',
  'keys',
  'config',
  'client',
  'cluster',
  'command',
  'geo',
  'pfdebug',
  'pfselftest',
].map(item => item.toLowerCase());

const CommentCountChart: FC<Props> = ({data}) => {
  const [chartData, setChartData] = useState<ChartData[]>([]);
  const [commentCount, setCommentCount] = useState<number>(0);

  useEffect(() => {
    let count = 0;
    const chartData = data?.map(item => {
      if (ignoredRedisCommands.includes(item.name.toLowerCase())) {
        return null;
      }
      count += parseInt(item.value);
      return {
        type: item.name,
        value: parseInt(item.value)
      } as ChartData;
    });
    const realChartData = chartData?.filter(item => item !== null) as ChartData[];
    console.log(realChartData)
    setChartData(realChartData);
    setCommentCount(count);
  }, [data]);

  const renderStatistic = (containerWidth: number, text: string, style: any) => {
    const {width: textWidth, height: textHeight} = measureTextWidth(text, style);
    const R = containerWidth / 2; // r^2 = (w / 2)^2 + (h - offsetY)^2

    let scale = 1;

    if (containerWidth < textWidth) {
      scale = Math.min(Math.sqrt(Math.abs(Math.pow(R, 2) / (Math.pow(textWidth / 2, 2) + Math.pow(textHeight, 2)))), 1);
    }

    const textStyleStr = `width:${containerWidth}px;`;
    return `<div style="${textStyleStr};font-size:${scale}em;line-height:${scale < 1 ? 1 : 'inherit'};">${text}</div>`;
  }


  const config = {
    data: chartData,
    animate: false,
    appendPadding: 10,
    angleField: 'value',
    colorField: 'type',
    paddingRight: 80,
    innerRadius: 0.6,
    label: {
      text: 'value',
      style: {
        fontWeight: 'bold',
      },
    },
    legend: {
      color: {
        title: true,
        position: 'left',
        // rowPadding: 100,
      },
    },
    annotations: [
      {
        type: 'text',
        style: {
          text: `${commentCount}`,
          x: '50%',
          y: '50%',
          textAlign: 'center',
          fontSize: 40,
          fontStyle: 'bold',
        },
      },
    ],
  };
  return (
    <ProCard
      style={{marginTop: 15}}
      title={
        <span>命令统计
        <span style={{color: 'gray', fontSize: '12px', marginLeft: '10px'}}>
        (仅统计主要命令)
        </span>
      </span>}
    >
      <Pie {...config}/>
    </ProCard>
  )
}

export default CommentCountChart;
