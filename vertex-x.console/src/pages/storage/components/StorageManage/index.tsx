import { Button, Card, Col, Divider, Row, Space, Descriptions, DescriptionsProps } from "antd";
import styles from './index.less';
import aswSvg from "./assets/aws.svg";
import folder from "./assets/folder.svg";
import cos from "./assets/cos.svg";
import oss from "./assets/oss.svg";
import minio from "./assets/minio.svg";
import sftp from "./assets/sftp.svg";
import onedrive from "./assets/onedrive.svg";
import { FC } from "react";

const StorageManage = () => {
  return (
    <Card bordered={false} style={{boxShadow: "none"}}>
      <div className={styles.wrapper}>
        <Row>
          <Col span={24}>
            <StorageStrategy title="服务器磁盘" icon={folder} hiddenDelete/>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={12}>
            <StorageStrategy title="亚马逊 S3 云存储" icon={aswSvg}/>
          </Col>
          <Col span={12}>
            <StorageStrategy title="腾讯云 COS" icon={cos}/>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={12}>
            <StorageStrategy title="阿里云 OSS" icon={oss}/>
          </Col>
          <Col span={12}>
            <StorageStrategy title="MINIO" icon={minio}/>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={12}>
            <StorageStrategy title="SFTP" icon={sftp}/>
          </Col>
          <Col span={12}>
            <StorageStrategy title="微软 OneDrive" icon={onedrive}/>
          </Col>
        </Row>
      </div>
    </Card>
  )
}

interface StorageStrategyProps {
  title: string;
  icon: string;
  actionDisabled?: boolean;
  hiddenDelete?: boolean;
  onEdit?: () => any;
  onDelete?: () => any;
}
const StorageStrategy: FC<StorageStrategyProps> = (props) => {
  const {title, icon, actionDisabled = true, hiddenDelete, onEdit, onDelete} = props;
  const items: DescriptionsProps['items'] = [
    {
      key: 'region',
      label: 'Region',
      children: <p>ap-shanghai</p>,
    },
    {
      key: 'endpoint',
      label: 'Endpoint',
      children: <p>cos.ap-shanghai.myqcloud.com</p>,
    },
    {
      key: 'bucket',
      label: 'bucket',
      children: <p>cos-1panel-backup-1301333510</p>,
    },
    {
      key: 'storageType',
      label: '存储类型',
      children: <p>深度归档存储</p>,
    },
    {
      key: 'backupPath',
      label: '备份路径',
      children: <p>未设置</p>,
    },
    {
      key: 'createTime',
      label: '创建时间',
      children: <p>2023-09-14 22:44:12</p>,
    },
  ];
  return (
    <div className={styles['storage-strategy-wrapper']}>
      <div className={styles.header}>
        <section className={styles.left}>
          <img src={icon} alt=""/>
          <span>{title}</span>
        </section>
        <section className={styles.right}>
          <Space>
            <Button shape="round" disabled={actionDisabled}>编辑</Button>
            {!hiddenDelete && <Button shape="round" disabled={actionDisabled}>删除</Button>}
          </Space>
        </section>
      </div>
      <Divider/>
      {!true ? (
        <div className={styles.placeholder}>
          <Button shape="round" size="large">添加 {title}</Button>
        </div>
      ) : (
        <div className={styles.body}>
          <Descriptions
            className={styles.descriptions}
            column={1}
            items={items}
          />
        </div>
      )}
    </div>
  )
}

export default StorageManage;
