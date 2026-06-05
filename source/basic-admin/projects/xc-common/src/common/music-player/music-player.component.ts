import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonTool} from '@ccxc/tool';

@Component({
  selector: 'app-music-player',
  templateUrl: './music-player.component.html',
  styleUrls: ['./music-player.component.scss']
})
export class MusicPlayerComponent implements OnInit, OnDestroy {

  public playList: Array<{ id, name, url }> = [];
  public currentMusic: { id, name, url } = {id: '', name: '', url: ''};
  public audio;
  public progress: number; // 进度效果
  public suspend = true;  // 暂停开始
  public mute = false; // 是否静音
  public volumeNum: number; // 音量
  public playType: string; // 播放类型
  public interval: any;
  public isPlayList = false;

  constructor() {
    this.playType = 'list-loop';
    this.progress = 0;
    const body = document.getElementsByTagName('body');
    if (body.length > 0) {
      const player = document.getElementById('xc-music-player');
      if (CommonTool.notNull(player)) {
        this.audio = player;
        this.progress = this.audio.currentTime;
        this.currentMusic = {id: '0', name: player['name'], url: this.audio.src};
        this.playList.push(this.currentMusic);
        this.suspend = false;
        this.calculationProgress();
      } else {
        this.audio = document.createElement('audio');
        this.audio.id = 'xc-music-player';
        this.audio.className = 'xc-music';
      }
      this.volumeNum = this.audio.volume * 100;
      body[0].appendChild(this.audio);
      this.audio.onended = () => {
        this.playEnd();
      };
      this.audio.onpause = () => {
        this.suspend = true;
      };
    }
  }

  ngOnInit() {
  }

  /**
   * 播放结束
   */
  public playEnd() {
    this.progress = 0;
    this.suspend = true;
    const playList = this.playList;
    if (this.playType === 'list-loop') {
      // 顺序播放
      for (let i = 0; i < this.playList.length; i++) {
        if (this.playList[i].id === this.currentMusic.id) {
          if (CommonTool.notNull(this.playList[i + 1])) {
            this.play(this.playList[i + 1].id);
          } else {
            this.play(this.playList[0].id);
          }
          break;
        }
      }
    } else if (this.playType === 'single-loop') {
      // 单曲播放
      this.play(this.currentMusic.id);
    } else if (this.playType === 'random') {
      // 随机播放
      const id = this.getRandomId(playList, this.currentMusic.id);
      this.play(id);
    }
  }

  /**
   * 下一曲
   */
  public nextSong(id) {
    if (this.playType === 'single-loop') {
      this.play(id);
      return;
    }
    if (this.playType === 'list-loop') {
      let playId = id;
      this.playList.forEach((ref, i) => {
        if (id === ref.id) {
          const play = this.playList[i + 1];
          if (CommonTool.notNull(play)) {
            playId = play.id;
          } else {
            playId = 1;
          }
        }
      });
      this.play(playId);
      return;
    }
    // 随机播放
    if (this.playType === 'random') {
      const playId = this.getRandomId(this.playList, id);
      this.play(playId);
      return;
    }
  }

  /**
   * 上一曲
   */
  public lastSong(id) {
    if (this.playType === 'single-loop') {
      this.play(id);
      return;
    }
    if (this.playType === 'list-loop') {
      let playId = id;
      this.playList.forEach((ref, i) => {
        if (id === ref.id) {
          const play = this.playList[i - 1];
          if (CommonTool.notNull(play)) {
            playId = play.id;
          } else {
            playId = this.playList.length;
          }
        }
      });
      this.play(playId);
      return;
    }
    // 随机播放
    if (this.playType === 'random') {
      const playId = this.getRandomId(this.playList, id);
      this.play(playId);
      return;
    }
  }

  /**
   * 获取不包含当前歌曲的id
   */
  public getRandomId(playList, id) {
    const temp = [];
    // 应该在不包括当前的列表中播放
    for (let i = 0; i < playList.length; i++) {
      if (playList[i].id !== id) {
        temp.push(playList[i].id);
      }
    }
    const random = parseInt(String(Math.random() * temp.length));
    return temp[random];
  }

  /**
   * 设置播放列表
   */
  public setPlayList(playList) {
    const array = [];
    let id = 0;
    playList.forEach((ref, index) => {
      if (index === 0) {
        id = index + 1;
      }
      array.push({
        id: index + 1,
        name: ref.name,
        url: ref.url
      });
    });
    this.playList = array;
    return id;
  }

  /**
   * 添加音乐
   */
  public addMusic(music) {
    this.isPlayList = true;
    if (!CommonTool.notNull(this.playList)) {
      this.playList = [];
    }
    let id;
    if (this.playList.length === 0) {
      id = 1;
    } else {
      id = this.playList.length + 1;
    }
    const array = [];
    this.playList.forEach(ref => {
      array.push(ref);
    });
    array.push({
      id,
      name: music.name,
      url: music.url
    });
    this.playList = array;
    return id;
  }

  /**
   * 播放
   */
  public play(id?) {
    this.isPlayList = true;
    this.progress = 0;
    if (CommonTool.notNull(id)) {
      const music = this.getMusic(id);
      this.audio.src = music.url;
      this.audio.name = music.name;
      this.currentMusic = music;
      this.suspend = false;
      this.audio.play();
      this.calculationProgress();
    } else {
      if (this.playList.length > 1) {
        const music = this.playList[0];
        this.audio.src = music.url;
        this.audio.name = music.name;
        this.currentMusic = music;
        this.suspend = false;
        this.audio.play();
        this.calculationProgress();
      }
    }
  }

  /**
   * 根据id获取音乐对象
   */
  public getMusic(id) {
    for (const music of this.playList) {
      if (music.id === id) {
        return music;
      }
    }
  }

  /**
   * 计算进度
   */
  public calculationProgress() {
    if (CommonTool.notNull(this.interval)) {
      clearInterval(this.interval);
    }
    this.interval = setInterval(() => {
      const buffer = this.audio.buffered.length;
      if (!this.suspend && buffer > 0) {
        this.progress++;
      }
      if (this.progress >= this.audio.duration) {
        clearInterval(this.interval);
      }
    }, 1000);
  }

  /**
   * 进度
   */
  public progressChange($event) {
    this.audio.currentTime = $event;
  }

  /**
   * 音量调节
   */
  public volumeSize($event) {
    this.audio.volume = $event / 100;
  }

  /**
   * 播放顺序
   */
  public playOrder(playType) {
    if (playType === 'list-loop') {
      this.playType = 'random';
    } else if (playType === 'random') {
      this.playType = 'single-loop';
    } else if (playType === 'single-loop') {
      this.playType = 'list-loop';
    }
  }

  /**
   * 暂停播放，false：暂停，true：播放
   */
  public stop(suspend) {
    if (CommonTool.notNull(this.currentMusic.id)) {
      if (suspend) {
        this.suspend = false;
        this.audio.play();
      } else {
        this.audio.pause();
      }
    }
  }

  ngOnDestroy(): void {
    this.stop(false);
  }
}
