import React from 'react';
import styles from './TowerLoader.module.css';

const TowerLoader = () => {
  return (
    <div className={styles.loader}>
      <div className={`${styles.box} ${styles['box-1']}`}>
        <div className={styles['side-left']}></div>
        <div className={styles['side-right']}></div>
        <div className={styles['side-top']}></div>
      </div>
      <div className={`${styles.box} ${styles['box-2']}`}>
        <div className={styles['side-left']}></div>
        <div className={styles['side-right']}></div>
        <div className={styles['side-top']}></div>
      </div>
      <div className={`${styles.box} ${styles['box-3']}`}>
        <div className={styles['side-left']}></div>
        <div className={styles['side-right']}></div>
        <div className={styles['side-top']}></div>
      </div>
      <div className={`${styles.box} ${styles['box-4']}`}>
        <div className={styles['side-left']}></div>
        <div className={styles['side-right']}></div>
        <div className={styles['side-top']}></div>
      </div>
    </div>
  )
}

export default TowerLoader;
