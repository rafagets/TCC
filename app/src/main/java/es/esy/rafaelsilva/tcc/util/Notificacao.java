package es.esy.rafaelsilva.tcc.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.AvaliacaoActivity;
import es.esy.rafaelsilva.tcc.activity.HistoricoActivity;

/**
 * Criado por Rafael em 18/11/2016, enjoy it.
 */
public class Notificacao {
    public int NOTIFICATION_ID; // Um inteiro, que identifica cada notificação de forma exclusiva
    private Context contexto;

    public Notificacao(Context contexto, int NOTIFICATION_ID) {
        this.contexto = contexto;
        this.NOTIFICATION_ID = NOTIFICATION_ID;
    }

    /**
     * Enviar notificação simples usando a API NotificationCompat.
     */
    public void enviar(int codigoCompra, String titulo, String corpo, String subCorpo) {

        // Use NotificationCompat.Builder para configurar nossa notificação.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto);

        //Ícone aparece na barra de notificação do dispositivo e no canto direito da notificação
        builder.setSmallIcon(R.mipmap.ic_launcher);

        // Esta intenção é disparada quando a notificação é clicada
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stacktips.com/"));
        Intent intent = new Intent(contexto, AvaliacaoActivity.class);
        intent.putExtra("codigo", codigoCompra);
        intent.putExtra("pai", NOTIFICATION_ID);
        PendingIntent pendingIntent = PendingIntent.getActivity(contexto, 0, intent, 0);

        // Definir a intenção que será acionado quando o usuário toca a notificação.
        builder.setContentIntent(pendingIntent);

        // O ícone grande aparece à esquerda da notificação
        builder.setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.mipmap.ic_launcher));

        // Título do conteúdo, que aparece em letras grandes na parte superior da notificação
        builder.setContentTitle(titulo + codigoCompra);

        // Texto de conteúdo, que aparece em texto menor abaixo do título
        builder.setContentText(corpo);

        /*O subtexto, que aparece sob o texto em dispositivos mais novos.
        Isso aparecerá nos dispositivos com Android 4.2 e superior apenas*/
        builder.setSubText(subCorpo);

        NotificationManager notificationManager = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
