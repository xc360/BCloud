/**
 * 配置
 */
export class Config {
  public onended: any; // 播放结束回调,配置后，需自己实现播放下一首

  constructor(data: any = {}) {
    this.onended = data.onended;
  }
}

/**
 * 音乐对象
 */
export class Music {
  public name; // 音乐名称
  public id; // 音乐地址

  constructor(data: any = {}) {
    this.name = data.name;
    this.id = data.id;
  }
}
