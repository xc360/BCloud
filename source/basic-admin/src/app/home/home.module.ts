import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {
  NzAvatarModule,
  NzBackTopModule,
  NzButtonModule,
  NzDropDownModule,
  NzIconModule,
  NzInputModule,
  NzLayoutModule,
  NzMenuModule, NzMessageModule,
  NzModalModule
} from 'ng-zorro-antd';
import {DefaultPageComponent} from './home-page/default-page.component';
import {HomeRoutingModule} from './home-routing.module';
import {HomeComponent} from './home.component';
import {FormInfoModule, PipeModule} from '../../../projects/xc-common/src/public-api';
import {OverallSelectModule} from '../../../projects/xc-common/src/public-api';
import {NzFormModule} from 'ng-zorro-antd/form';

@NgModule({
  declarations: [HomeComponent, DefaultPageComponent],
  imports: [
    CommonModule,
    FormsModule,
    HomeRoutingModule,
    NzIconModule,
    NzMenuModule,
    NzButtonModule,
    NzLayoutModule,
    NzAvatarModule,
    NzDropDownModule,
    NzModalModule,
    NzInputModule,
    NzBackTopModule,
    PipeModule,
    NzMessageModule,
    OverallSelectModule,
    NzFormModule,
    FormInfoModule
  ]
})
export class HomeModule {
}
