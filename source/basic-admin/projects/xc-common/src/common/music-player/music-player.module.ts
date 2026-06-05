import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MusicPlayerComponent} from './music-player.component';
import {NzProgressModule} from 'ng-zorro-antd/progress';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzSliderModule} from 'ng-zorro-antd/slider';
import {FormsModule} from '@angular/forms';
import { NzListModule } from 'ng-zorro-antd/list';
@NgModule({
  declarations: [MusicPlayerComponent],
  exports: [
    MusicPlayerComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    NzButtonModule,
    NzProgressModule,
    NzIconModule,
    NzSliderModule,
    NzListModule
  ]
})
export class MusicPlayerModule {
}
