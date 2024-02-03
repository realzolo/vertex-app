import { Space } from "antd";
import Process from "./Process";
import GarbageCollection from "./GarbageCollection";
import HeapMemory from "./HeapMemory";
import styles from "./index.less";

const RealtimeInformation = () => {
  return (
    <div className={styles.wrapper}>
      <div className={styles['left-wrapper']}>
        <Space direction="vertical">
          <Process/>
          <GarbageCollection/>
          <HeapMemory/>
        </Space>
      </div>
      <div className={styles['right-wrapper']}></div>
    </div>
  )
}

export default RealtimeInformation;
