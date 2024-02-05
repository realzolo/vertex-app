import React, { useEffect, useState } from 'react';
import styles from './index.less';
import { getAppVersion } from "@/services/application/app.api";

const DEV_START_YEAR = 2023;

const Footer = () => {
  const [yearRange, setYearRange] = useState("");
  const [version, setVersion] = useState("V1.0.0");

  useEffect(() => {
    getAppVersion().then((res) => {
      setVersion(res.data);
    });
  }, []);

  useEffect(() => {
    const currentYear = new Date().getFullYear();
    if (currentYear > DEV_START_YEAR) {
      setYearRange(`${DEV_START_YEAR}-${currentYear}`);
    } else {
      setYearRange(`${DEV_START_YEAR}`);
    }
  }, []);

  return (
    <footer className={styles.wrapper}>
      <a href="https://github.com/realzolo/vertex-app">Copyright © {yearRange} VERTEX.APP {version ?? '@' + version}</a>
    </footer>
  );
};

export default Footer;
