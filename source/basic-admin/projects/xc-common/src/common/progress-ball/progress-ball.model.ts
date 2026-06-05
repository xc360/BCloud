export class ProgressBallModel {
  public showValue: string | number; // 显示的值
  public size?: number; // 大小
  public progress?: number; // 进度,百分比,最小0，最大100
  public animSpeed?: number; // 动画速度，单位秒
  public unit?: string; // 单位
  public waterColor?: string; // 动画颜色
  public fontColor?: string; // 字体颜色
  public fontSize?: number; // 字体大小
  public backgroundColor?: string; // 背景颜色
}
