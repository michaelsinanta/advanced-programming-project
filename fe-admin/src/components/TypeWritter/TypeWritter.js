import React from "react";
import styles from "./TypeWritter.module.css";

const TypeWriter = () => {
  return (
    <div className={styles.typewriter}>
      <div className={styles.slide}>
        <i></i>
      </div>
      <div className={styles.paper}></div>
      <div className={styles.keyboard}></div>
    </div>
  );
};

export default TypeWriter;
