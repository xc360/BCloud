import {Pipe, PipeTransform} from '@angular/core';
import {CommonTool} from '@ccxc/tool';

@Pipe({
  name: 'basic'
})
export class BasicPipe implements PipeTransform {

  constructor() {
  }

  transform(value: any, type: any): any {
    if (type === 'space') {
      return CommonTool.getSpace(value);
    }

  }
}
