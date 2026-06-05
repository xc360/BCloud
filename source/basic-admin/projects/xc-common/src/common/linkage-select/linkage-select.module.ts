import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LinkageSelectComponent} from './linkage-select.component';
import {NzSelectModule} from 'ng-zorro-antd';
import {FormsModule} from '@angular/forms';
import {PipeModule} from '../pipe/pipe.module';


@NgModule({
  declarations: [LinkageSelectComponent],
  imports: [
    CommonModule,
    NzSelectModule,
    FormsModule,
    PipeModule
  ],
  exports: [LinkageSelectComponent]
})
export class LinkageSelectModule {
}
