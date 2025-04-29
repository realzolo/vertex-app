import boxen from 'boxen'
import picocolors from 'picocolors'
import type { Plugin } from 'vite'

export default function appInfo(): Plugin {
  return {
    name: 'appInfo',
    apply: 'serve',
    async buildStart() {
      const { bold, green, cyan, bgGreen, underline } = picocolors
      // eslint-disable-next-line no-console
      console.log(
        boxen(
          `${bold(green(`${bgGreen('Vertx App v1.0.0')}`))}\n${cyan('官网：')}${underline('https://vertx.onezol.com')}\n`,
          {
            padding: 1,
            margin: 1,
            borderStyle: 'double',
            textAlignment: 'center',
          },
        ),
      )
    },
  }
}
