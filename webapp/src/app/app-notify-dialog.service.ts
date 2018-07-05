import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';

export interface NotifyEvent {
    title: string;
    message: string;
    description: string;
}

@Injectable()
export class AppNotifyDialogService {

    public notify: Subject<NotifyEvent> = new Subject<NotifyEvent>();

    public open(message: string): void {
        const event: NotifyEvent = {
            title: 'Notify',
            message: message,
            description: ''
        };
        this.notify.next(event);
      }

      public openWidthTitle(title: string, message: string): void {
        const event: NotifyEvent = {
            title: title,
            message: message,
            description: ''
        };
        this.notify.next(event);
      }

      public openWidthDescription(title: string, message: string, description: string): void {
        const event: NotifyEvent = {
            title: title,
            message: message,
            description: description
        };
        this.notify.next(event);
      }
}

