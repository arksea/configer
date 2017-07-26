import { ConfigServerPage } from './app.po';

describe('config-server App', () => {
  let page: ConfigServerPage;

  beforeEach(() => {
    page = new ConfigServerPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
